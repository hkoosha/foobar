@file:Suppress("unused")

package io.koosha.foobar.warehouse.api.model

import io.koosha.foobar.warehouse.API_PREFIX
import java.io.Serializable
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.persistence.Version


@Entity
@Table(name = "${API_PREFIX}__${AvailabilityDO.ENTITY_TYPE}")
open class AvailabilityDO(

    @EmbeddedId
    open var availabilityPk: AvailabilityPk = AvailabilityPk(),

    @Version
    @Column(name = "VERSION")
    open var version: Long? = null,

    @Column(
        name = "CREATED",
        nullable = false,
    )
    open var created: ZonedDateTime? = null,

    @Column(
        name = "UPDATED",
        nullable = false,
    )
    open var updated: ZonedDateTime? = null,

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

    companion object {
        const val ENTITY_TYPE = "availability"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as AvailabilityDO
        return this.availabilityPk == rhs.availabilityPk
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "availabilityPk=" + this.availabilityPk +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ", unitsAvailable=" + this.unitsAvailable +
            ", frozenUnits=" + this.frozenUnits +
            ", pricePerUnit=" + this.pricePerUnit +
            ")"


    @Embeddable
    open class AvailabilityPk(

        @Column(
            name = "SELLER_ID",
            nullable = false,
            length = 36,
        )
        @org.hibernate.annotations.Type(type = "uuid-char")
        open var sellerId: UUID? = null,

        @ManyToOne(optional = false)
        open var product: ProductDO? = null,

        ) : Serializable {

        override fun equals(other: Any?): Boolean {
            if (this === other)
                return true
            if (this.javaClass != other?.javaClass)
                return false
            val rhs = other as AvailabilityPk
            return this.sellerId != null
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
