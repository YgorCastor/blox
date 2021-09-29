package me.ycastor.btc.domain.marketplace.adapters.coinprices.coingecko

import arrow.core.Either
import drewcarlson.coingecko.CoinGeckoClient
import drewcarlson.coingecko.error.CoinGeckoApiError
import drewcarlson.coingecko.error.CoinGeckoApiException
import drewcarlson.coingecko.models.coins.CoinFullData
import drewcarlson.coingecko.models.coins.MarketData
import me.ycastor.btc.domain.marketplace.core.errors.CoinPriceServiceError
import me.ycastor.btc.domain.marketplace.core.errors.InvalidCurrencyException
import me.ycastor.btc.domain.marketplace.core.models.CoinMarketInformation
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoinPricesInformation
import java.math.BigDecimal
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response.Status.Family

@ApplicationScoped
class CoinPricesGeckoService(private val client: CoinGeckoClient) : FetchCoinPricesInformation {
    override suspend fun bySymbol(coin: Coin) =
        Either.catch { client.getCoinById(id = coin.name).toCoinInformation(coin, "eur") }
            .mapLeft { it.toError() }

    private fun CoinFullData.toCoinInformation(coin: Coin, currency: String) = CoinMarketInformation(
        coin = coin,
        price = this.marketData.forCurrency(currency),
    )

    private fun MarketData?.forCurrency(currency: String): BigDecimal =
        this?.currentPrice
            ?.get(currency)
            ?.toBigDecimal() ?: throw InvalidCurrencyException(currency)

    private fun Throwable.toError() = when (this) {
        is InvalidCurrencyException -> CoinPriceServiceError.InvalidRequest(this.message ?: "Invalid Currency")
        is CoinGeckoApiException -> this.error.toServiceError()
        else -> CoinPriceServiceError.ServerErrorPrice(this.message ?: "Unknown Error")
    }

    private fun CoinGeckoApiError?.toServiceError() = when (Family.familyOf(this?.code ?: 500)) {
        Family.CLIENT_ERROR -> CoinPriceServiceError.InvalidRequest(this?.message ?: "Unknown Request Failure")
        else -> CoinPriceServiceError.ServerErrorPrice(this?.message ?: "Unknown Error")
    }

}
