package me.ycastor.btc.domain.marketplace.core.commands

import java.math.BigDecimal

data class PlaceOrderCommand(
    val coinCode: String,
    val quantity: Int,
    val buyAtPrice: BigDecimal,
)
