package me.ycastor.btc.domain.marketplace.adapters.coinprices.coingecko

import drewcarlson.coingecko.CoinGeckoClient
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces

@ApplicationScoped
class CoinGeckoClientProducer {
    @Produces
    fun coinGeckoClient(): CoinGeckoClient = CoinGeckoClient.create()
}
