@file:Suppress("unused")

package io.koosha.foobar.seller.api.model.entity

import io.koosha.foobar.seller.api.model.SellerState
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "seller__seller")
@EntityListeners(AuditingEntityListener::class)
open class SellerDO(
    @Id
    @Column(
        name = "SELLER_ID",
        length = 36,
    )
    // @org.hibernate.annotations.Type(type = "uuid-char")
    open var sellerId: UUID? = null,

    @Version
    @Column(name = "VERSION")
    open var version: Long? = null,

    @CreatedDate
    @Column(
        name = "CREATED",
        nullable = false,
    )
    open var created: Instant? = null,

    @LastModifiedDate
    @Column(
        name = "UPDATED",
        nullable = false,
    )
    open var updated: Instant? = null,

    @Column(
        name = "NAME",
        nullable = false,
    )
    open var name: String? = null,

    @Embedded
    open var address: AddressDO = AddressDO(),

    @Enumerated(EnumType.STRING)
    @Column(
        name = "STATE",
        nullable = false,
        length = 32,
    )
    open var state: SellerState? = null,
) {

    fun detachedCopy(): SellerDO = SellerDO(
        sellerId = this.sellerId,
        version = this.version,
        created = this.created,
        updated = this.updated,
        name = this.name,
        address = this.address,
        state = this.state,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        val rhs = other as? SellerDO
        return rhs != null
                && this.sellerId != null
                && this.sellerId == rhs.sellerId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "sellerId=" + this.sellerId +
            ", state=" + this.state +
            ", name=" + this.name +
            ", address=" + this.address +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"

}
