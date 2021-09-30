package me.ycastor.btc.domain.charges.core

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import java.math.BigDecimal
import java.util.UUID
import javax.enterprise.context.ApplicationScoped
import me.ycastor.btc.domain.charges.core.errors.ChargesError
import me.ycastor.btc.domain.charges.core.models.OperationFee
import me.ycastor.btc.domain.charges.core.ports.input.FetchCoinChargeUseCase
import me.ycastor.btc.domain.charges.core.ports.output.FetchCoinOperationFee

@ApplicationScoped
class CoinChargesManager(val fetchCoinOperationFee: FetchCoinOperationFee) : FetchCoinChargeUseCase {

    override suspend fun calculateChargeFor(
        coinId: UUID,
        quantity: Int,
        unitPrice: BigDecimal,
    ): Either<ChargesError, OperationFee> =
        fetchCoinOperationFee.forCoin(coinId)?.let {
            val coinBuyPrice = unitPrice * quantity.toBigDecimal()
            val totalFee = (coinBuyPrice * it.percentage) / BigDecimal(100)
            OperationFee(percentage = it.percentage, totalFee).right()
        } ?: ChargesError.NoChargeRegistered(coinId).left()
}
