package me.ycastor.btc.domain.marketplace.adapters.market

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.coroutines.asFlow
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.UUID
import javax.enterprise.context.ApplicationScoped
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import me.ycastor.btc.domain.marketplace.core.ports.output.CoinPersistence
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoin

@ApplicationScoped
class CoinRepository : CoinPersistence,
                       FetchCoin,
                       PanacheRepositoryBase<Coin, UUID> {

    override suspend fun save(coin: Coin): Coin {
        return this.persist(coin).awaitSuspending()
    }

    override suspend fun remove(id: UUID) {
        this.deleteById(id).awaitSuspending()
    }

    override suspend fun byId(id: UUID): Coin? = this.findById(id).awaitSuspending()

    override suspend fun byCode(coinCode: String): Coin? =
        this.find("code = ?1", coinCode).singleResult<Coin>().awaitSuspending()

    override fun all(): Multi<Coin> {
        return this.streamAll()
    }
}
