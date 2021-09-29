package me.ycastor.btc.domain.marketplace.core.ports.input

import arrow.core.Either
import me.ycastor.btc.domain.marketplace.core.commands.SupportedCoinAdd
import me.ycastor.btc.domain.marketplace.core.commands.SupportedCoinToggle
import me.ycastor.btc.domain.marketplace.core.errors.CoinMarketError
import me.ycastor.btc.domain.marketplace.core.models.entities.Coin
import java.util.*

interface ManageCoinUseCase {
    suspend fun addSupportedCoin(command: SupportedCoinAdd): Either<CoinMarketError, Coin>
    suspend fun removeSupportedCoin(id: UUID): Either<CoinMarketError, Coin>
    suspend fun toggleCoinSupportState(command: SupportedCoinToggle): Either<CoinMarketError, Coin>
}
