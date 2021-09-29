package me.ycastor.btc.domain.marketplace.core.commands

import me.ycastor.btc.domain.marketplace.core.models.entities.Coin

data class PurchaseCalculationCommand(
    val coin: Coin,
    val quantity: Int,
)
