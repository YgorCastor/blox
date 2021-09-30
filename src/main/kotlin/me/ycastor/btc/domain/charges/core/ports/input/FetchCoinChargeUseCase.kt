package me.ycastor.btc.domain.charges.core.ports.input

import arrow.core.Either
import java.math.BigDecimal
import java.util.UUID
import me.ycastor.btc.domain.charges.core.errors.ChargesError
import me.ycastor.btc.domain.charges.core.models.OperationFee

interface FetchCoinChargeUseCase {
    suspend fun calculateChargeFor(
        coinId: UUID,
        quantity: Int,
        unitPrice: BigDecimal,
    ): Either<ChargesError, OperationFee>
}
