package me.ycastor.btc.domain.marketplace.core.ports.input

import arrow.core.Either
import me.ycastor.btc.commons.errorhandler.Error
import me.ycastor.btc.domain.marketplace.core.commands.PlaceOrderCommand
import me.ycastor.btc.domain.marketplace.core.commands.PurchaseCalculationCommand
import me.ycastor.btc.domain.marketplace.core.models.CoinPurchaseRequestInfo
import me.ycastor.btc.domain.marketplace.core.models.entities.Order
import java.util.*

interface BuyCoinUseCase {
    suspend fun calculatePurchasePrice(command: PurchaseCalculationCommand): Either<Error, CoinPurchaseRequestInfo>

    suspend fun placeOrder(userId: UUID, command: PlaceOrderCommand): Either<Error, Order>
}
