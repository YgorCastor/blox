package me.ycastor.btc.domain.charges.core.errors

import io.netty.handler.codec.http.HttpResponseStatus
import java.util.UUID
import me.ycastor.btc.commons.errorhandler.Error

sealed class ChargesError(
    override val code: String,
    override val status: Int,
    override val message: String,
    override val details: List<String> = emptyList(),
) : Error(code, message, status, details) {
    data class NoChargeRegistered(val coinId: UUID) : ChargesError(
        code = "CHRGSV_INVALID_REQUEST",
        status = HttpResponseStatus.NOT_FOUND.code(),
        message = "No Fee charge registered for the coin '$coinId'",
    )
}
