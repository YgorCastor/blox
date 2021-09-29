package me.ycastor.btc.domain.marketplace.core.ports.output

import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import java.util.*

interface CoinPersistence {
    suspend fun save(coin: Coin): Coin

    suspend fun remove(id: UUID)
}
