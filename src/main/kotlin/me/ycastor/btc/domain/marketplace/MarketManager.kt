package me.ycastor.btc.domain.marketplace

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.smallrye.mutiny.Multi
import javax.enterprise.context.ApplicationScoped
import kotlinx.coroutines.flow.Flow
import me.ycastor.btc.domain.marketplace.core.errors.CoinMarketError
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import me.ycastor.btc.domain.marketplace.core.ports.input.CoinFetchUseCase
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoin

@ApplicationScoped
class MarketManager(
    val coinFetcher: FetchCoin,
) : CoinFetchUseCase {
    override fun availableCoins(): Multi<Coin> = coinFetcher.all()

    override suspend fun fetchCoinWithCode(coinCode: String): Either<CoinMarketError.CoinNotFound, Coin> =
        coinFetcher.byCode(coinCode)?.right() ?: CoinMarketError.CoinNotFound(id = null, coinCode = coinCode).left()
}
