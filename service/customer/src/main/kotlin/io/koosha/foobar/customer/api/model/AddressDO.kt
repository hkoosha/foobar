@file:Suppress("unused")

package io.koosha.foobar.customer.api.model

import io.koosha.foobar.customer.API_PREFIX
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import jakarta.persistence.Version
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime


@Entity
@Table(name = "${API_PREFIX}__${AddressDO.ENTITY_TYPE}")
@EntityListeners(AuditingEntityListener::class)
open class AddressDO(

    @EmbeddedId
    open var addressPk: Pk = Pk(),

    @Column(name = "VERSION")
    @Version
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
        name = "NAME",
        nullable = false
    )
    @Size(min = 1, max = 127)
    open var name: String? = null,

    @Column(
        name = "ZIP_CODE",
        nullable = false,
    )
    @Size(max = 127)
    open var zipcode: String? = null,

    @Column(
        name = "ADDRESS_LINE_1",
        nullable = false
    )
    @Size(max = 127)
    open var addressLine1: String? = null,

    @Column(
        name = "COUNTRY",
        nullable = false
    )
    @Size(max = 127)
    open var country: String? = null,

    @Column(
        name = "CITY",
        nullable = false
    )
    @Size(max = 127)
    open var city: String? = null,

    ) {

    companion object {
        const val ENTITY_TYPE = "address"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as AddressDO
        return this.addressPk == rhs.addressPk
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "addressId=" + this.addressPk +
            ", name=" + this.name +
            ", zipCode=" + this.zipcode +
            ", addressLine1=" + this.addressLine1 +
            ", country=" + this.country +
            ", city=" + this.city +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"


    @Embeddable
    open class Pk(

        @Column(
            name = "ADDRESS_ID",
            nullable = false,
        )
        open var addressId: Long? = null,

        @ManyToOne(optional = false)
        open var customer: CustomerDO? = null,

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
            return this.addressId != null
                    && this.customer?.customerId != null
                    && this.addressId == rhs.addressId
                    && this.customer?.customerId == rhs.customer?.customerId
        }

        override fun hashCode(): Int = this.javaClass.hashCode()

        override fun toString(): String = this.javaClass.simpleName + "(" +
                "customerId=" + this.customer?.customerId +
                ", addressId=" + this.addressId +
                ")"

    }

}
