package me.ycastor.btc.domain.marketplace.core.models

import java.math.BigDecimal

data class CoinPurchaseRequestInfo(
    val market: CoinMarketInformation,
    val quantity: Int,
    val feePrice: BigDecimal,
) {
    val priceWithoutFee get() = market.price * quantity.toBigDecimal()
    val totalPrice get() = priceWithoutFee + feePrice
}
