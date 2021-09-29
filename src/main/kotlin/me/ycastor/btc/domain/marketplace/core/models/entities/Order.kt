package me.ycastor.btc.domain.marketplace.core.models.entities

import me.ycastor.btc.domain.marketplace.core.models.OrderStatus
import org.hibernate.Hibernate
import java.math.BigDecimal
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Order(
    @Id
    var id: UUID = UUID.randomUUID(),
    var coinId: UUID,
    var userId: UUID,
    var quantity: Int,
    var buyAtPrice: BigDecimal,
    var orderStatus: OrderStatus,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Order

        return id == other.id
    }

    override fun hashCode(): Int = 749_455_228

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , coinId = $coinId , userId = $userId , quantity = $quantity , coinPrice = $buyAtPrice , orderStatus = $orderStatus )"
    }

}
