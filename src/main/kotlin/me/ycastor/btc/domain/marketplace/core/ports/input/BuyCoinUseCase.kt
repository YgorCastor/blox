package me.ycastor.btc.domain.marketplace.core.ports.input

import arrow.core.Either
import me.ycastor.btc.commons.errorhandler.Error
import me.ycastor.btc.domain.marketplace.core.commands.PurchaseCalculationCommand
import me.ycastor.btc.domain.marketplace.core.models.CoinPurchaseRequestInfo

interface BuyCoinUseCase {
    suspend fun calculatePurchasePrice(command: PurchaseCalculationCommand): Either<Error, CoinPurchaseRequestInfo>
}
