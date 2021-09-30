package me.ycastor.btc.domain.marketplace.adapters.market

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.UUID
import me.ycastor.btc.domain.marketplace.core.models.entities.Order
import me.ycastor.btc.domain.marketplace.core.ports.output.OrderPlacement

class OrderRepository : OrderPlacement,
                        PanacheRepositoryBase<Order, UUID> {

    override suspend fun placeOrder(order: Order): Order = this.persist(order).awaitSuspending()

}
