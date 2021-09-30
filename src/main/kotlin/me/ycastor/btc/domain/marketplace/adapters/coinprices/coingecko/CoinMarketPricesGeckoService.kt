package me.ycastor.btc.domain.marketplace.adapters.coinprices.coingecko

import arrow.core.Either
import drewcarlson.coingecko.CoinGeckoClient
import drewcarlson.coingecko.error.CoinGeckoApiError
import drewcarlson.coingecko.error.CoinGeckoApiException
import drewcarlson.coingecko.models.coins.CoinFullData
import drewcarlson.coingecko.models.coins.MarketData
import io.quarkus.arc.properties.IfBuildProperty
import java.math.BigDecimal
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.core.Response.Status.Family
import me.ycastor.btc.domain.marketplace.core.errors.CoinPriceServiceError
import me.ycastor.btc.domain.marketplace.core.errors.InvalidCurrencyException
import me.ycastor.btc.domain.marketplace.core.models.CoinMarketInformation
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoinMarketPricesInformation

@ApplicationScoped
@IfBuildProperty(name = "blox.market.engine", stringValue = "coingecko")
class CoinMarketPricesGeckoService(private val client: CoinGeckoClient) : FetchCoinMarketPricesInformation {

    override suspend fun byCode(code: String) =
        Either.catch { client.getCoinById(id = code).toCoinInformation("eur") }
            .mapLeft { it.toError() }

    private fun CoinFullData.toCoinInformation(currency: String) = CoinMarketInformation(
        coin = this.name,
        price = this.marketData.forCurrency(currency),
    )

    private fun MarketData?.forCurrency(currency: String): BigDecimal =
        this?.currentPrice
            ?.get(currency)
            ?.toBigDecimal() ?: throw InvalidCurrencyException(currency)

    private fun Throwable.toError() = when (this) {
        is InvalidCurrencyException -> CoinPriceServiceError.InvalidRequest("Invalid Currency", this.message)
        is CoinGeckoApiException    -> this.error.toServiceError(this.message)
        else                        -> CoinPriceServiceError.ServerErrorPrice("Unknown Error", this.message)
    }

    private fun CoinGeckoApiError?.toServiceError(exceptionMessage: String) =
        when (Family.familyOf(this?.code ?: 500)) {
            Family.CLIENT_ERROR -> CoinPriceServiceError.InvalidRequest(this?.message, exceptionMessage)
            else                -> CoinPriceServiceError.ServerErrorPrice(this?.message, exceptionMessage)
        }

}
