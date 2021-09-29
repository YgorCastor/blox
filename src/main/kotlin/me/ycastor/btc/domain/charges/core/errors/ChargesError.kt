package me.ycastor.btc.domain.charges.core.errors

import io.netty.handler.codec.http.HttpResponseStatus
import me.ycastor.btc.commons.errorhandler.Error
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin

sealed class ChargesError(
    override val code: String,
    override val status: Int,
    override val message: String,
    override val details: List<String> = emptyList(),
) : Error(code, message, status, details) {
    data class NoChargeRegistered(val coin: Coin) : ChargesError(
        code = "CHRGSV_INVALID_REQUEST",
        status = HttpResponseStatus.NOT_FOUND.code(),
        message = "No Fee charge registered for the coin '$coin'",
    )
}
