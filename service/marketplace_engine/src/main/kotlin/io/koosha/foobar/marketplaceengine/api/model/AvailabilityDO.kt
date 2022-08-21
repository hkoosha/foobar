package io.koosha.foobar.marketplaceengine.api.model

import io.koosha.foobar.marketplaceengine.API_PREFIX
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table
import javax.persistence.Version


@Entity
@Table(name = "${API_PREFIX}__availability")
open class AvailabilityDO(

    @EmbeddedId
    open var availabilityPk: Pk = Pk(),

    @Version
    @Column(name = "VERSION")
    open var version: Long? = null,

    @CreatedDate
    @Column(
        name = "CREATED",
        nullable = false,
    )
    open var created: ZonedDateTime? = null,

    @LastModifiedDate
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

    @PrePersist
    fun updateCreatedAt() {
        this.created = ZonedDateTime.now(ZoneOffset.UTC)
        this.updated = ZonedDateTime.now(ZoneOffset.UTC)
    }

    @PreUpdate
    fun updateUpdatedAt() {
        this.updated = ZonedDateTime.now(ZoneOffset.UTC)
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
    open class Pk(

        @Column(
            name = "SELLER_ID",
            nullable = false,
            length = 36,
        )
        @org.hibernate.annotations.Type(type = "uuid-char")
        open var sellerId: UUID? = null,

        @Column(
            name = "PRODUCT_ID",
            nullable = false,
            length = 36,
        )
        @org.hibernate.annotations.Type(type = "uuid-char")
        open var productId: UUID? = null,

        ) : Serializable {

        companion object {
            private const val serialVersionUID = 0L
        }

        override fun equals(other: Any?): Boolean {
            if (this === other)
                return true
            if (this.javaClass != other?.javaClass)
                return false
            val rhs = other as Pk
            return this.sellerId != null
                    && this.productId != null
                    && this.sellerId == rhs.sellerId
                    && this.productId == rhs.productId
        }

        @Suppress("RedundantOverride")
        override fun hashCode(): Int = super.hashCode()

        override fun toString(): String = this.javaClass.simpleName + "(" +
                "sellerId=" + this.sellerId +
                ", productId=" + this.productId +
                ")"

    }

}
