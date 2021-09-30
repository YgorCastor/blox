package me.ycastor.btc.domain.marketplace.core.models.entities

import org.hibernate.Hibernate
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "Coins")
data class Coin(
    @Id
    var id: UUID = UUID.randomUUID(),
    var name: String,
    var code: String,
    var enabled: Boolean = false,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Coin

        return id == other.id
    }

    override fun hashCode(): Int = 869_444_921

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id , name = $name , code = $code , enabled = $enabled )"
    }

}
