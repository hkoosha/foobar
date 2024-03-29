@file:Suppress("unused")

package io.koosha.foobar.warehouse.api.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Version
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serial
import java.io.Serializable
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "warehouse__availability")
@EntityListeners(AuditingEntityListener::class)
open class AvailabilityDO(
    @EmbeddedId
    open var availabilityPk: AvailabilityPk = AvailabilityPk(),

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
        name = "UNITS_AVAILABLE",
        nullable = false,
    )
    open var unitsAvailable: Long? = null,

    @Column(
        name = "FROZEN_UNITS",
        nullable = false,
    )
    open var frozenUnits: Long? = null,

    @Column(
        name = "PRICE_PER_UNIT",
        nullable = false,
    )
    open var pricePerUnit: Long? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        val rhs = other as? AvailabilityDO
        return rhs != null
                && this.availabilityPk == rhs.availabilityPk
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "availabilityPk=" + this.availabilityPk +
            ", unitsAvailable=" + this.unitsAvailable +
            ", frozenUnits=" + this.frozenUnits +
            ", pricePerUnit=" + this.pricePerUnit +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"


    @Embeddable
    open class AvailabilityPk(

        @Column(
            name = "SELLER_ID",
            nullable = false,
            length = 36,
        )
        // @org.hibernate.annotations.Type(type = "uuid-char")
        open var sellerId: UUID? = null,

        @ManyToOne(optional = false)
        open var product: ProductDO? = null,
    ) : Serializable {

        companion object {
            @Serial
            private const val serialVersionUID = 0L
        }

        override fun equals(other: Any?): Boolean {
            if (this === other)
                return true
            val rhs = other as? AvailabilityPk
            return rhs != null
                    && this.sellerId != null
                    && this.product != null
                    && this.sellerId == rhs.sellerId
                    && this.product == rhs.product
        }

        @Suppress("RedundantOverride")
        override fun hashCode(): Int = super.hashCode()

        override fun toString(): String = this.javaClass.simpleName + "(" +
                "sellerId=" + this.sellerId +
                ", productId=" + this.product?.productId +
                ")"

    }

}
