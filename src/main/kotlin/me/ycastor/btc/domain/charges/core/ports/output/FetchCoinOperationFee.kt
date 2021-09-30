package me.ycastor.btc.domain.charges.core.ports.output

import java.util.UUID
import me.ycastor.btc.domain.charges.core.models.entities.Fee

interface FetchCoinOperationFee {
    suspend fun forCoin(coinId: UUID): Fee?
}
