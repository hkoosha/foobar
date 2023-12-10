@file:Suppress("unused")

package io.koosha.foobar.shipping.api.model.entity

import io.koosha.foobar.shipping.api.model.ShippingState
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
@Table(name = "shipping__shipping")
@EntityListeners(AuditingEntityListener::class)
open class ShippingDO(

    @Id
    @Column(
        name = "SHIPPING_ID",
        length = 36,
    )
    // @org.hibernate.annotations.Type(type = "uuid-char")
    open var shippingId: UUID? = null,

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
        name = "ORDER_REQUEST_ID",
        length = 36,
    )
    // @org.hibernate.annotations.Type(type = "uuid-char")
    open var orderRequestId: UUID? = null,

    @Embedded
    open var pickupAddress: AddressDO = AddressDO(),

    @Embedded
    open var deliveryAddress: AddressDO = AddressDO(),

    @Enumerated(EnumType.STRING)
    @Column(
        name = "STATE",
        nullable = false,
        length = 32,
    )
    open var state: ShippingState? = null,
) {

    fun detachedCopy(): ShippingDO = ShippingDO(
        shippingId = this.shippingId,
        version = this.version,
        created = this.created,
        updated = this.updated,
        orderRequestId = this.orderRequestId,
        pickupAddress = this.pickupAddress.detachedCopy(),
        deliveryAddress = this.deliveryAddress.detachedCopy(),
        state = this.state,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        val rhs = other as? ShippingDO
        return rhs != null
                && this.shippingId != null
                && this.shippingId == rhs.shippingId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "shippingId=" + this.shippingId +
            ", pickupAddress=" + this.pickupAddress +
            ", deliveryAddress=" + this.deliveryAddress +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"

}
