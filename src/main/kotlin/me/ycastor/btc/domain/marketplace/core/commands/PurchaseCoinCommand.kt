package me.ycastor.btc.domain.marketplace.core.commands

data class PurchaseCoinCommand(
    val coin: String,
    val quantity: String,
    val wallet: String,
)
