package me.ycastor.btc.domain.marketplace.core.errors

import io.netty.handler.codec.http.HttpResponseStatus
import me.ycastor.btc.commons.errorhandler.Error
import java.util.*

sealed class CoinMarketError(
    code: String,
    message: String,
    status: Int,
    details: List<String> = emptyList()
) : Error(code, message, status, details) {
    data class CoinNotFound(val id: UUID) : CoinMarketError(
        code = "MRKT_COIN_NOT_FOUND",
        status = HttpResponseStatus.NOT_FOUND.code(),
        message = "No coin found for the id '$id'"
    )

    data class PersistenceError(val cause: String?) : CoinMarketError(
        code = "MRKT_PERSISTENCE_FAILURE",
        status = HttpResponseStatus.INTERNAL_SERVER_ERROR.code(),
        message = "Failure while connecting to the database!",
        details = cause?.let { listOf(it) } ?: emptyList(),
    )
}
