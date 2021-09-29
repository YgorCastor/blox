package me.ycastor.btc.domain.marketplace.core.ports.output

import me.ycastor.btc.domain.marketplace.core.models.entities.Order
import java.util.*

interface OrderPlacement {
    suspend fun placeOrder(order: Order): Order
}
