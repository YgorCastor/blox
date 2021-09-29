package me.ycastor.btc.domain.marketplace.core.errors

import io.netty.handler.codec.http.HttpResponseStatus
import me.ycastor.btc.commons.errorhandler.Error

sealed class CoinPriceServiceError(
    override val code: String,
    override val status: Int,
    override val message: String,
    override val details: List<String> = emptyList(),
) : Error(code, message, status, details) {
    data class InvalidRequest(val cause: String) : CoinPriceServiceError(
        code = "COINSVC_INVALID_REQUEST",
        status = HttpResponseStatus.BAD_REQUEST.code(),
        message = "Invalid request to the Coin Market Service",
        details = listOf(cause),
    )

    data class ServerErrorPrice(val cause: String) : CoinPriceServiceError(
        code = "COINSVC_SERVER_ERROR",
        status = HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
        message = "The Coin Market Service had an unexpected error",
        details = listOf(cause),
    )
}
