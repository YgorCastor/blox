package me.ycastor.btc.domain.marketplace.adapters.market

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.smallrye.mutiny.coroutines.asFlow
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import me.ycastor.btc.domain.marketplace.core.ports.output.CoinPersistence
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoin
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CoinRepository : CoinPersistence, FetchCoin, PanacheRepositoryBase<Coin, UUID> {

    override suspend fun save(coin: Coin): Coin {
        return this.persist(coin).awaitSuspending()
    }

    override suspend fun remove(id: UUID) {
        this.deleteById(id).awaitSuspending()
    }

    override suspend fun byId(id: UUID): Coin? = this.findById(id).awaitSuspending()

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun all(): Flow<Coin> {
        return this.streamAll().asFlow()
    }
}
