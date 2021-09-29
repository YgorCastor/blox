package me.ycastor.btc.domain.marketplace.core.models

import java.util.*

data class OrderInformation(
    val orderId: UUID,
    val status: OrderStatus,
)
