package me.ycastor.btc.domain.marketplace.core.ports.output

import arrow.core.Either
import me.ycastor.btc.domain.marketplace.core.errors.CoinPriceServiceError
import me.ycastor.btc.domain.marketplace.core.models.CoinMarketInformation
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin

interface FetchCoinPricesInformation {
    suspend fun bySymbol(coin: Coin): Either<CoinPriceServiceError, CoinMarketInformation>
}
