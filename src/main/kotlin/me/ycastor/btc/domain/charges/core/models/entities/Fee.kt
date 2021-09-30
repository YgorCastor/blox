package me.ycastor.btc.domain.charges.core.models.entities

import java.math.BigDecimal
import java.util.UUID
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import org.hibernate.Hibernate

@Entity
@Table(name = "Fees")
data class Fee(
    @Id
    var id: UUID,
    var coinId: UUID,
    var percentage: BigDecimal,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Fee

        return id == other.id
    }

    override fun hashCode(): Int = 158_664_852

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , coinId = $coinId , percentageFee = $percentage )"
    }
}
