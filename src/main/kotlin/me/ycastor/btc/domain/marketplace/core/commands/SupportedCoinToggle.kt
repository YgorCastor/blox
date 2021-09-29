package me.ycastor.btc.domain.marketplace.core.commands

import java.util.*

data class SupportedCoinToggle(
    val id: UUID,
    val enabled: Boolean,
)
