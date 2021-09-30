package me.ycastor.btc.domain.marketplace.adapters.coinprices.coingecko

import drewcarlson.coingecko.CoinGeckoClient
import io.quarkus.arc.properties.IfBuildProperty
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces

@ApplicationScoped
@IfBuildProperty(name = "blox.market.engine", stringValue = "coingecko")
class CoinGeckoClientProducer {
    @Produces
    fun coinGeckoClient(): CoinGeckoClient = CoinGeckoClient.create()
}
