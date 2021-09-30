package me.ycastor.btc.domain.marketplace.adapters.coinprices.noop

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.quarkus.arc.properties.IfBuildProperty
import java.math.BigDecimal
import javax.enterprise.context.ApplicationScoped
import me.ycastor.btc.domain.marketplace.core.errors.CoinPriceServiceError
import me.ycastor.btc.domain.marketplace.core.models.CoinMarketInformation
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoinMarketPricesInformation

@ApplicationScoped
@IfBuildProperty(name = "blox.market.engine", stringValue = "noop")
class NoopCoinMarketPricesService : FetchCoinMarketPricesInformation {
    override suspend fun byCode(code: String): Either<CoinPriceServiceError, CoinMarketInformation> = when (code) {
        "bitcoin" -> CoinMarketInformation(
            coin = "Bitcoin",
            price = BigDecimal(100), // Haha, i wish
        ).right()
        else      -> CoinPriceServiceError.InvalidRequest("Currency $code not found", cause = null).left()
    }
}