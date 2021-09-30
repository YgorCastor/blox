package me.ycastor.btc.domain.charges.core.models

import java.math.BigDecimal

data class OperationFee(
    val percentage: BigDecimal,
    val totalFee: BigDecimal,
)
