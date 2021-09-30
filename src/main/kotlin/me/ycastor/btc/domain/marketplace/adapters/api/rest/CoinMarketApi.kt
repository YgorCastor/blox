package me.ycastor.btc.domain.marketplace.adapters.api.rest

import java.util.UUID
import javax.ws.rs.Consumes
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import me.ycastor.btc.commons.errorhandler.ApplicationException
import me.ycastor.btc.domain.marketplace.core.commands.PlaceOrderCommand
import me.ycastor.btc.domain.marketplace.core.commands.PurchaseCalculationCommand
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import me.ycastor.btc.domain.marketplace.core.ports.input.BuyCoinUseCase
import me.ycastor.btc.domain.marketplace.core.ports.input.CoinFetchUseCase

@Path("/api/market/coin/v1")
class CoinMarketApi(
    private val buyCoinUseCase: BuyCoinUseCase,
    private val coinFetchUseCase: CoinFetchUseCase,
) {
    @Path("/")
    @GET
    @Produces(APPLICATION_JSON)
    fun fetchAvailableCoins() = coinFetchUseCase.availableCoins()

    @Path("/{code}")
    @GET
    @Produces(APPLICATION_JSON)
    suspend fun fetchCoinWithCode(code: String): Coin =
        coinFetchUseCase.fetchCoinWithCode(code)
            .fold({ throw ApplicationException(it) }, { coin -> coin })

    @Path("/calculate-price")
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    suspend fun calculatePrice(command: PurchaseCalculationCommand) =
        buyCoinUseCase.calculatePurchasePrice(command)
            .fold({ throw ApplicationException(it) }, { coinPurchaseInfo -> coinPurchaseInfo })

    @Path("/place-order")
    @POST
    @Produces(APPLICATION_JSON)
    @Consumes(APPLICATION_JSON)
    suspend fun placeOrder(command: PlaceOrderCommand) =
        buyCoinUseCase.placeOrder(
            userId = UUID.fromString("a8d6b490-a1ff-46b1-b990-c494f5be381b"),
            command = command,
        ).fold({ throw ApplicationException(it) }, { order -> order })

}
