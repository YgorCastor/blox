package me.ycastor.btc.domain.marketplace

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.runBlocking
import me.ycastor.btc.domain.charges.core.errors.ChargesError
import me.ycastor.btc.domain.charges.core.models.OperationFee
import me.ycastor.btc.domain.charges.core.ports.input.FetchCoinChargeUseCase
import me.ycastor.btc.domain.marketplace.core.commands.PurchaseCalculationCommand
import me.ycastor.btc.domain.marketplace.core.errors.CoinPriceServiceError
import me.ycastor.btc.domain.marketplace.core.models.CoinMarketInformation
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoinPricesInformation
import me.ycastor.btc.domain.marketplace.fixtures.CoinFixture
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.math.BigDecimal
import kotlin.test.assertEquals

@ExtendWith(MockKExtension::class)
internal class CoinOperationsCalculatorTest {

    @MockK
    lateinit var fetchCoinChargeUseCase: FetchCoinChargeUseCase

    @MockK
    lateinit var fetchCoinPricesInformation: FetchCoinPricesInformation

    @InjectMockKs
    lateinit var coinOperationsCalculator: CoinOperationsCalculator

    @Test
    internal fun `Calculate a purchase price correctly`() = runBlocking {
        val coin = CoinFixture.bitcoin()

        val command = PurchaseCalculationCommand(coin = coin, quantity = 10)
        val coinMarketInformation = CoinMarketInformation(coin = coin, price = BigDecimal.ONE)
        val priceWithoutFee = coinMarketInformation.price * BigDecimal.TEN
        val operationFee = OperationFee(totalFee = BigDecimal.ONE)
        val totalPrice = priceWithoutFee + operationFee.totalFee

        coEvery { fetchCoinPricesInformation.bySymbol(coin) } returns coinMarketInformation.right()
        every {
            fetchCoinChargeUseCase.calculateChargeFor(
                coin = coin,
                quantity = 10,
                unitPrice = coinMarketInformation.price,
            )
        } returns operationFee.right()

        val result = coinOperationsCalculator.calculatePurchasePrice(command).orNull()

        assertEquals(actual = result?.market, expected = coinMarketInformation)
        assertEquals(actual = result?.feePrice, expected = operationFee.totalFee)
        assertEquals(actual = result?.priceWithoutFee, expected = priceWithoutFee)
        assertEquals(actual = result?.totalPrice, expected = totalPrice)
    }

    @Test
    internal fun `If a problem occurs in while fetching the market information, an error should be returned`() =
        runBlocking {
            val coin = CoinFixture.bitcoin()
            val command = PurchaseCalculationCommand(coin, quantity = 10)
            val expectedError = CoinPriceServiceError.ServerErrorPrice("KBOOM!")

            coEvery { fetchCoinPricesInformation.bySymbol(coin) } returns expectedError.left()

            val result =
                coinOperationsCalculator.calculatePurchasePrice(command).fold({ it }, { throw RuntimeException("NOP") })

            assertEquals(actual = result, expected = expectedError)
        }

    @Test
    internal fun `If a problem occurs in while fetching the fee calculation, an error should be returned`() =
        runBlocking {
            val coin = CoinFixture.bitcoin()
            val command = PurchaseCalculationCommand(coin, quantity = 10)
            val coinMarketInformation = CoinMarketInformation(coin, price = BigDecimal.ONE)
            val expectedError = ChargesError.NoChargeRegistered(coin)

            coEvery { fetchCoinPricesInformation.bySymbol(coin) } returns coinMarketInformation.right()
            every {
                fetchCoinChargeUseCase.calculateChargeFor(
                    coin = coin,
                    quantity = 10,
                    unitPrice = coinMarketInformation.price,
                )
            } returns expectedError.left()

            val result =
                coinOperationsCalculator.calculatePurchasePrice(command).fold({ it }, { throw RuntimeException("NOP") })

            assertEquals(actual = result, expected = expectedError)
        }
}
