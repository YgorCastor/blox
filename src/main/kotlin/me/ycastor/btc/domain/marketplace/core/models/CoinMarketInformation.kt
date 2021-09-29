package me.ycastor.btc.domain.marketplace.core.models

import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import java.math.BigDecimal

data class CoinMarketInformation(
    val coin: Coin,
    val price: BigDecimal,
)
