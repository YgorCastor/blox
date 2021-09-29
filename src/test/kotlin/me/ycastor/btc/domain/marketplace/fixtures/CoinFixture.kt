package me.ycastor.btc.domain.marketplace.fixtures

import me.ycastor.btc.domain.marketplace.core.models.entities.Coin

object CoinFixture {
    fun bitcoin() = Coin(
        name = "Bitcoin",
        code = "btc",
    )
}