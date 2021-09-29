package me.ycastor.btc.domain.marketplace.adapters.coinprices.coingecko

import io.quarkus.test.junit.QuarkusTest
import javax.inject.Inject

@QuarkusTest
internal class CoinMarketGeckoServiceIT {

    @Inject
    lateinit var coinMarketGeckoService: CoinPricesGeckoService


}