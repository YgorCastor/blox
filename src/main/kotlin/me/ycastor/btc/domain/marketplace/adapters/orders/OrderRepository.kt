package me.ycastor.btc.domain.marketplace.adapters.orders

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.UUID
import javax.enterprise.context.ApplicationScoped
import me.ycastor.btc.domain.marketplace.core.models.entities.Order
import me.ycastor.btc.domain.marketplace.core.ports.output.OrderPlacement

@ApplicationScoped
class OrderRepository : OrderPlacement,
                        PanacheRepositoryBase<Order, UUID> {
    override suspend fun placeOrder(order: Order): Order = this.persist(order).awaitSuspending()
}
