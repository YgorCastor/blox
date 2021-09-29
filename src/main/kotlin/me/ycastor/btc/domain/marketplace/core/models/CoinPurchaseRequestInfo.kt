package me.ycastor.btc.domain.marketplace.core.models

import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import java.math.BigDecimal

data class CoinPurchaseRequestInfo(
    val coin: Coin,
    val market: CoinMarketInformation,
    val quantity: Int,
    val feePrice: BigDecimal,
) {
    val priceWithoutFee get() = market.price * quantity.toBigDecimal()
    val totalPrice get() = priceWithoutFee + feePrice
}
