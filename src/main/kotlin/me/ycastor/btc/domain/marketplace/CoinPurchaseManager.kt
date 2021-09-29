package me.ycastor.btc.domain.marketplace

import arrow.core.*
import me.ycastor.btc.commons.errorhandler.Error
import me.ycastor.btc.domain.charges.core.ports.input.FetchCoinChargeUseCase
import me.ycastor.btc.domain.marketplace.core.commands.PlaceOrderCommand
import me.ycastor.btc.domain.marketplace.core.commands.PurchaseCalculationCommand
import me.ycastor.btc.domain.marketplace.core.errors.CoinMarketError
import me.ycastor.btc.domain.marketplace.core.errors.CoinPriceServiceError
import me.ycastor.btc.domain.marketplace.core.errors.OrderPlacementError
import me.ycastor.btc.domain.marketplace.core.models.CoinMarketInformation
import me.ycastor.btc.domain.marketplace.core.models.CoinPurchaseRequestInfo
import me.ycastor.btc.domain.marketplace.core.models.OrderStatus
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import me.ycastor.btc.domain.marketplace.core.models.entities.Order
import me.ycastor.btc.domain.marketplace.core.ports.input.BuyCoinUseCase
import me.ycastor.btc.domain.marketplace.core.ports.input.CoinFetchUseCase
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoinMarketPricesInformation
import me.ycastor.btc.domain.marketplace.core.ports.output.OrderPlacement
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
internal class CoinPurchaseManager(
    private val fetchCoinChargeUseCase: FetchCoinChargeUseCase,
    private val fetchCoinMarketPricesInformation: FetchCoinMarketPricesInformation,
    private val coinFetchUseCase: CoinFetchUseCase,
    private val orderPlacement: OrderPlacement,
) : BuyCoinUseCase {

    override suspend fun calculatePurchasePrice(command: PurchaseCalculationCommand): Either<Error, CoinPurchaseRequestInfo> {
        val coin = coinFetchUseCase.withCode(command.coinCode)?.right()
            ?: CoinMarketError.CoinNotFound(id = null, coinCode = command.coinCode).left()
        val marketInformation = fetchCoinMarketPricesInformation.byCode(command.coinCode)
        val feeInfo = coin.flatMap { marketInformation.calculateOperationFee(it, command.quantity) }

        return marketInformation.zip(feeInfo, coin) { market, fee, theCoin ->
            CoinPurchaseRequestInfo(
                coin = theCoin,
                market = market,
                quantity = command.quantity,
                feePrice = fee.totalFee,
            )
        }
    }

    override suspend fun placeOrder(userId: UUID, command: PlaceOrderCommand): Either<Error, Order> = Either.catch {
        coinFetchUseCase.withCode(command.coinCode)?.let {
            return orderPlacement.placeOrder(
                Order(
                    coinId = it.id,
                    userId = userId,
                    quantity = command.quantity,
                    buyAtPrice = command.buyAtPrice,
                    orderStatus = OrderStatus.PLACED,
                )
            ).right()
        } ?: CoinMarketError.CoinNotFound(id = null, coinCode = command.coinCode).left()
    }.mapLeft { OrderPlacementError.FailedToPlaceOrder(it.message) }.flatten()


    private fun Either<CoinPriceServiceError, CoinMarketInformation>.calculateOperationFee(coin: Coin, quantity: Int) =
        this.flatMap {
            fetchCoinChargeUseCase.calculateChargeFor(coin.id, quantity, it.price)
        }
}
