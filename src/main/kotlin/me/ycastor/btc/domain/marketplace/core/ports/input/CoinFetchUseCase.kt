package me.ycastor.btc.domain.marketplace.core.ports.input

import kotlinx.coroutines.flow.Flow
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin

interface CoinFetchUseCase {
    suspend fun availableCoins(): Flow<Coin>

    suspend fun withCode(coinCode: String): Coin?
}