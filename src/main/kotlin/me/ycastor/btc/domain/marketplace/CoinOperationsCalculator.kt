package me.ycastor.btc.domain.marketplace

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.zip
import me.ycastor.btc.commons.errorhandler.Error
import me.ycastor.btc.domain.charges.core.ports.input.FetchCoinChargeUseCase
import me.ycastor.btc.domain.marketplace.core.commands.PurchaseCalculationCommand
import me.ycastor.btc.domain.marketplace.core.errors.CoinPriceServiceError
import me.ycastor.btc.domain.marketplace.core.models.CoinMarketInformation
import me.ycastor.btc.domain.marketplace.core.models.CoinPurchaseRequestInfo
import me.ycastor.btc.domain.marketplace.core.ports.input.BuyCoinUseCase
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoinPricesInformation
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
internal class CoinOperationsCalculator(
    private val fetchCoinChargeUseCase: FetchCoinChargeUseCase,
    private val fetchCoinPricesInformation: FetchCoinPricesInformation,
) : BuyCoinUseCase {

    override suspend fun calculatePurchasePrice(command: PurchaseCalculationCommand): Either<Error, CoinPurchaseRequestInfo> {
        val marketInformation = fetchCoinPricesInformation.bySymbol(command.coin)
        val feeInfo = marketInformation.calculateOperationFee(command.quantity)

        return marketInformation.zip(feeInfo) { market, fee ->
            CoinPurchaseRequestInfo(
                market = market,
                quantity = command.quantity,
                feePrice = fee.totalFee,
            )
        }
    }

    private fun Either<CoinPriceServiceError, CoinMarketInformation>.calculateOperationFee(quantity: Int) =
        this.flatMap {
            fetchCoinChargeUseCase.calculateChargeFor(it.coin, quantity, it.price)
        }
}
