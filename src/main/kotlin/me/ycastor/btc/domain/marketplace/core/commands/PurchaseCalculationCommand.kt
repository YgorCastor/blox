package me.ycastor.btc.domain.marketplace.core.commands

data class PurchaseCalculationCommand(
    val coinCode: String,
    val quantity: Int,
)
