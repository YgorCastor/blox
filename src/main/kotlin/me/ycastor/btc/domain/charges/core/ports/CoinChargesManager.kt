package me.ycastor.btc.domain.charges.core.ports

import arrow.core.Either
import me.ycastor.btc.domain.charges.core.errors.ChargesError
import me.ycastor.btc.domain.charges.core.models.OperationFee
import me.ycastor.btc.domain.charges.core.ports.input.FetchCoinChargeUseCase
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import java.math.BigDecimal
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class CoinChargesManager : FetchCoinChargeUseCase {
    override fun calculateChargeFor(
        coin: Coin,
        quantity: Int,
        unitPrice: BigDecimal,
    ): Either<ChargesError, OperationFee> {
        TODO("Not yet implemented")
    }
}
