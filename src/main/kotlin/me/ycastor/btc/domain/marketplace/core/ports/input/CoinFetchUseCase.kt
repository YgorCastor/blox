package me.ycastor.btc.domain.marketplace.core.ports.input

import arrow.core.Either
import io.smallrye.mutiny.Multi
import me.ycastor.btc.domain.marketplace.core.errors.CoinMarketError
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin

interface CoinFetchUseCase {
    fun availableCoins(): Multi<Coin>

    suspend fun fetchCoinWithCode(coinCode: String): Either<CoinMarketError.CoinNotFound, Coin>
}