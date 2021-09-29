package me.ycastor.btc.domain.marketplace.core.ports.output

import arrow.core.Either
import me.ycastor.btc.domain.marketplace.core.errors.CoinPriceServiceError
import me.ycastor.btc.domain.marketplace.core.models.CoinMarketInformation

interface FetchCoinMarketPricesInformation {
    suspend fun byCode(code: String): Either<CoinPriceServiceError, CoinMarketInformation>
}
