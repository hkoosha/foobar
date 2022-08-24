@file:Suppress("unused")

package io.koosha.foobar.shipping.api.model

import io.koosha.foobar.shipping.API_PREFIX
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version


@Entity
@Table(name = "${API_PREFIX}__${ShippingDO.ENTITY_TYPE}")
@EntityListeners(AuditingEntityListener::class)
open class ShippingDO(

    @Id
    @Column(
        name = "SHIPPING_ID",
        length = 36,
    )
    @org.hibernate.annotations.Type(type = "uuid-char")
    open var shippingId: UUID? = null,

    @Version
    @Column(name = "VERSION")
    open var version: Long? = null,

    @CreatedDate
    @Column(
        name = "CREATED",
        nullable = false,
    )
    open var created: LocalDateTime? = null,

    @LastModifiedDate
    @Column(
        name = "UPDATED",
        nullable = false,
    )
    open var updated: LocalDateTime? = null,

    @Column(
        name = "ORDER_REQUEST_ID",
        length = 36,
    )
    @org.hibernate.annotations.Type(type = "uuid-char")
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

    companion object {
        const val ENTITY_TYPE: String = "shipping"
    }

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
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as ShippingDO
        return this.shippingId != null
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
