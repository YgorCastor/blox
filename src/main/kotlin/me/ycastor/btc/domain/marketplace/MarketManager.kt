package me.ycastor.btc.domain.marketplace

import kotlinx.coroutines.flow.Flow
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import me.ycastor.btc.domain.marketplace.core.ports.input.CoinFetchUseCase
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoin
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MarketManager(
    val coinFetcher: FetchCoin,
) : CoinFetchUseCase {
    override suspend fun availableCoins(): Flow<Coin> = coinFetcher.all()

    override suspend fun withCode(coinCode: String): Coin? {
        TODO("Not yet implemented")
    }
}
