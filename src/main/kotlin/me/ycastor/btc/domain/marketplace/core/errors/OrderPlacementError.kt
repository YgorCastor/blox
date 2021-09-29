package me.ycastor.btc.domain.marketplace.core.errors

import io.netty.handler.codec.http.HttpResponseStatus
import me.ycastor.btc.commons.errorhandler.Error

sealed class OrderPlacementError(
    code: String,
    message: String,
    status: Int,
    details: List<String> = emptyList()
) : Error(code, message, status, details) {
    data class FailedToPlaceOrder(val cause: String?) : OrderPlacementError(
        code = "MRKT_ORDER_PLACE_ERROR",
        status = HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
        message = "Failed to place the order!",
        details = listOfNotNull(cause),
    )
}
