package me.ycastor.btc.domain.marketplace.core.ports.output

import kotlinx.coroutines.flow.Flow
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import java.util.*

interface FetchCoin {
    suspend fun byId(id: UUID): Coin?

    suspend fun all(): Flow<Coin>
}
