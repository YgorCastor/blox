package me.ycastor.btc.domain.charges.core.ports

import arrow.core.Either
import me.ycastor.btc.domain.charges.core.errors.ChargesError
import me.ycastor.btc.domain.charges.core.models.OperationFee
import me.ycastor.btc.domain.charges.core.ports.input.FetchCoinChargeUseCase
import java.math.BigDecimal
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CoinChargesManager : FetchCoinChargeUseCase {
    override fun calculateChargeFor(
        coinId: UUID,
        quantity: Int,
        unitPrice: BigDecimal
    ): Either<ChargesError, OperationFee> {
        TODO("Not yet implemented")
    }
}
