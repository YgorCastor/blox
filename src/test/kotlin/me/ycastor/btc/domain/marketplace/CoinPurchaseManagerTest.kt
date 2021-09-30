package me.ycastor.btc.domain.marketplace

import arrow.core.left
import arrow.core.right
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import java.math.BigDecimal
import java.util.UUID
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking
import me.ycastor.btc.domain.charges.core.errors.ChargesError
import me.ycastor.btc.domain.charges.core.models.OperationFee
import me.ycastor.btc.domain.charges.core.ports.input.FetchCoinChargeUseCase
import me.ycastor.btc.domain.marketplace.core.commands.PlaceOrderCommand
import me.ycastor.btc.domain.marketplace.core.commands.PurchaseCalculationCommand
import me.ycastor.btc.domain.marketplace.core.errors.CoinPriceServiceError
import me.ycastor.btc.domain.marketplace.core.models.CoinMarketInformation
import me.ycastor.btc.domain.marketplace.core.models.OrderStatus
import me.ycastor.btc.domain.marketplace.core.ports.input.CoinFetchUseCase
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoinMarketPricesInformation
import me.ycastor.btc.domain.marketplace.core.ports.output.OrderPlacement
import me.ycastor.btc.domain.marketplace.fixtures.CoinFixture
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
internal class CoinPurchaseManagerTest {

    @MockK
    lateinit var fetchCoinChargeUseCase: FetchCoinChargeUseCase

    @MockK
    lateinit var fetchCoinMarketPricesInformation: FetchCoinMarketPricesInformation

    @MockK
    lateinit var coinFetchUseCase: CoinFetchUseCase

    @MockK
    lateinit var orderPlacement: OrderPlacement

    @InjectMockKs
    lateinit var coinPurchaseManager: CoinPurchaseManager

    @Test
    internal fun `Should calculate a purchase price correctly`() = runBlocking {
        val coin = CoinFixture.bitcoin()

        val command = PurchaseCalculationCommand(coinCode = coin.code, quantity = 10)
        val coinMarketInformation = CoinMarketInformation(coin = coin.code, price = BigDecimal.ONE)
        val priceWithoutFee = coinMarketInformation.price * BigDecimal.TEN
        val operationFee = OperationFee(totalFee = BigDecimal.ONE, percentage = BigDecimal.ONE)
        val totalPrice = priceWithoutFee + operationFee.totalFee

        coEvery { coinFetchUseCase.fetchCoinWithCode(coin.code) } returns coin.right()
        coEvery { fetchCoinMarketPricesInformation.byCode(coin.code) } returns coinMarketInformation.right()
        coEvery {
            fetchCoinChargeUseCase.calculateChargeFor(
                coinId = coin.id,
                quantity = 10,
                unitPrice = coinMarketInformation.price,
            )
        } returns operationFee.right()

        val result = coinPurchaseManager.calculatePurchasePrice(command).orNull()

        assertEquals(actual = result?.market, expected = coinMarketInformation)
        assertEquals(actual = result?.feePrice, expected = operationFee.totalFee)
        assertEquals(actual = result?.priceWithoutFee, expected = priceWithoutFee)
        assertEquals(actual = result?.totalPrice, expected = totalPrice)
    }

    @Test
    internal fun `If a problem occurs in while fetching the market information, an error should be returned`() =
        runBlocking {
            val coin = CoinFixture.bitcoin()
            val command = PurchaseCalculationCommand(coin.code, quantity = 10)
            val expectedError = CoinPriceServiceError.ServerErrorPrice("KBOOM!")

            coEvery { coinFetchUseCase.fetchCoinWithCode(coin.code) } returns coin.right()
            coEvery { fetchCoinMarketPricesInformation.byCode(coin.code) } returns expectedError.left()

            val result =
                coinPurchaseManager.calculatePurchasePrice(command).fold({ it }, { throw RuntimeException("NOP") })

            assertEquals(actual = result, expected = expectedError)
        }

    @Test
    internal fun `If a problem occurs in while fetching the fee calculation, an error should be returned`() =
        runBlocking {
            val coin = CoinFixture.bitcoin()
            val command = PurchaseCalculationCommand(coin.code, quantity = 10)
            val coinMarketInformation = CoinMarketInformation(coin.code, price = BigDecimal.ONE)
            val expectedError = ChargesError.NoChargeRegistered(coin.id)

            coEvery { coinFetchUseCase.fetchCoinWithCode(coin.code) } returns coin.right()
            coEvery { fetchCoinMarketPricesInformation.byCode(coin.code) } returns coinMarketInformation.right()
            coEvery {
                fetchCoinChargeUseCase.calculateChargeFor(
                    coinId = coin.id,
                    quantity = 10,
                    unitPrice = coinMarketInformation.price,
                )
            } returns expectedError.left()

            val result =
                coinPurchaseManager.calculatePurchasePrice(command).fold({ it }, { throw RuntimeException("NOP") })

            assertEquals(actual = result, expected = expectedError)
        }

    @Test
    internal fun `Should place an order correctly`() = runBlocking {
        val bitcoin = CoinFixture.bitcoin()
        val userId = UUID.randomUUID()
        val command = PlaceOrderCommand(
            coinCode = bitcoin.code,
            quantity = 10,
            buyAtPrice = BigDecimal.valueOf(20),
        )

        coEvery { coinFetchUseCase.fetchCoinWithCode(bitcoin.code) } returns bitcoin.right()
        coEvery { orderPlacement.placeOrder(any()) } answers { firstArg() }

        val result = coinPurchaseManager.placeOrder(userId, command).orNull()

        assertEquals(actual = result?.buyAtPrice, expected = command.buyAtPrice)
        assertEquals(actual = result?.coinId, expected = bitcoin.id)
        assertEquals(actual = result?.orderStatus, expected = OrderStatus.PLACED)
        assertEquals(actual = result?.quantity, expected = command.quantity)
        assertEquals(actual = result?.userId, expected = userId)
    }
}
