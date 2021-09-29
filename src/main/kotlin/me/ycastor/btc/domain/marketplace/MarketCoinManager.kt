package me.ycastor.btc.domain.marketplace

import arrow.core.Either
import arrow.core.flatten
import arrow.core.left
import arrow.core.right
import kotlinx.coroutines.flow.Flow
import me.ycastor.btc.domain.marketplace.core.commands.SupportedCoinAdd
import me.ycastor.btc.domain.marketplace.core.commands.SupportedCoinToggle
import me.ycastor.btc.domain.marketplace.core.errors.CoinMarketError
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import me.ycastor.btc.domain.marketplace.core.ports.input.CoinInfoFetchUseCase
import me.ycastor.btc.domain.marketplace.core.ports.input.ManageCoinUseCase
import me.ycastor.btc.domain.marketplace.core.ports.output.CoinPersistence
import me.ycastor.btc.domain.marketplace.core.ports.output.FetchCoin
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MarketCoinManager(
    val coinPersistence: CoinPersistence,
    val coinFetcher: FetchCoin,
) : CoinInfoFetchUseCase, ManageCoinUseCase {

    override suspend fun availableCoins(): Flow<Coin> = coinFetcher.all()

    override suspend fun addSupportedCoin(command: SupportedCoinAdd): Either<CoinMarketError, Coin> = Either.catch {
        val newCoin = Coin(
            name = command.name,
            code = command.code,
        )
        coinPersistence.save(newCoin)
    }.mapLeft { CoinMarketError.PersistenceError(it.message) }

    override suspend fun removeSupportedCoin(id: UUID): Either<CoinMarketError, Coin> = Either.catch {
        coinFetcher.byId(id)?.let {
            coinPersistence.remove(id)
            it.right()
        } ?: CoinMarketError.CoinNotFound(id).left()
    }.mapLeft { CoinMarketError.PersistenceError(it.message) }.flatten()

    override suspend fun toggleCoinSupportState(command: SupportedCoinToggle): Either<CoinMarketError, Coin> =
        Either.catch {
            coinFetcher.byId(command.id)?.let {
                coinPersistence.save(it.copy(enabled = command.enabled)).right()
            } ?: CoinMarketError.CoinNotFound(command.id).left()
        }.mapLeft { CoinMarketError.PersistenceError(it.message) }.flatten()
}
