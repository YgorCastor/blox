package me.ycastor.btc.domain.marketplace.adapters.api.rest

import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.`when`
import io.restassured.RestAssured.given
import java.math.BigDecimal
import me.ycastor.btc.domain.marketplace.core.commands.PlaceOrderCommand
import me.ycastor.btc.domain.marketplace.core.commands.PurchaseCalculationCommand
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Test

@QuarkusTest
@TestHTTPEndpoint(CoinMarketApi::class)
internal class CoinMarketApiIT {

    @Test
    internal fun `Should be able to fetch all coins successfully`() {
        `when`().get()
            .then()
            .statusCode(200)
    }

    @Test
    internal fun `Should be able to fetch a coin by it's code`() {
        `when`().get("/bitcoin")
            .then()
            .statusCode(200)
            .body("id", equalTo("33ed5ea2-8f60-4ec2-8f20-b63779d73464"))
            .body("name", equalTo("Bitcoin"))
            .body("code", equalTo("bitcoin"))
            .body("enabled", equalTo(true))
    }

    @Test
    internal fun `Should fetch the price correctly`() {
        val command = PurchaseCalculationCommand(
            coinCode = "bitcoin",
            quantity = 1,
        )

        given()
            .contentType("application/json")
            .body(command)
            .`when`()
            .post("/calculate-price")
            .then()
            .statusCode(200)
            .body("market.price", equalTo(100))
            .body("quantity", equalTo(1))
            .body("feePrice", equalTo(5.2F))
            .body("priceWithoutFee", equalTo(100))
            .body("totalPrice", equalTo(105.2F))
    }

    @Test
    internal fun `Should place an order successfully`() {
        val command = PlaceOrderCommand(
            coinCode = "bitcoin",
            quantity = 10,
            buyAtPrice = BigDecimal(95),
        )

        given()
            .contentType("application/json")
            .body(command)
            .`when`()
            .post("/place-order")
            .then()
            .statusCode(200)
            .body("coinId", equalTo("33ed5ea2-8f60-4ec2-8f20-b63779d73464"))
            .body("userId", equalTo("a8d6b490-a1ff-46b1-b990-c494f5be381b"))
            .body("quantity", equalTo(10))
            .body("buyAtPrice", equalTo(95))
            .body("orderStatus", equalTo("PLACED"))
    }
}
