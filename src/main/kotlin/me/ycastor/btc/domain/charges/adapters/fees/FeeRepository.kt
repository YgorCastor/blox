package me.ycastor.btc.domain.charges.adapters.fees

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.UUID
import javax.enterprise.context.ApplicationScoped
import me.ycastor.btc.domain.charges.core.models.entities.Fee
import me.ycastor.btc.domain.charges.core.ports.output.FetchCoinOperationFee

@ApplicationScoped
class FeeRepository : FetchCoinOperationFee,
                      PanacheRepositoryBase<Fee, UUID> {
    override suspend fun forCoin(coinId: UUID): Fee? =
        this.find("coinId = ?1", coinId).singleResult<Fee?>().awaitSuspending()
}
