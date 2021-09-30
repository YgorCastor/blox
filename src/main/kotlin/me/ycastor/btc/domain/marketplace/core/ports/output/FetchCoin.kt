package me.ycastor.btc.domain.marketplace.core.ports.output

import io.smallrye.mutiny.Multi
import java.util.UUID
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin

interface FetchCoin {
    suspend fun byId(id: UUID): Coin?

    suspend fun byCode(coinCode: String): Coin?

    fun all(): Multi<Coin>
}
