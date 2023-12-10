@file:Suppress("unused")

package io.koosha.foobar.customer.api.model.entity

import io.koosha.foobar.customer.api.model.CustomerState
import io.koosha.foobar.customer.api.model.Title
import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import jakarta.validation.constraints.Size
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.Objects
import java.util.UUID

@Entity
@Table(name = "customer__customer")
@EntityListeners(AuditingEntityListener::class)
open class CustomerDO(
    @Id
    @Column(
        name = "CUSTOMER_ID",
        length = 36,
    )
    // @org.hibernate.annotations.Type(type = "uuid-char")
    @Size(min = 36, max = 36)
    open var customerId: UUID? = null,

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

    @Enumerated(EnumType.STRING)
    @Column(
        name = "STATE",
        nullable = false,
        length = 32,
    )
    open var state: CustomerState? = null,

    @Column(nullable = false)
    @Embedded
    open var name: NameDO = NameDO(),

    @Column(
        name = "ADDRESS_ID_POOL",
        nullable = false,
    )
    open var addressIdPool: Long? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        val rhs = other as? CustomerDO
        return rhs != null
                && this.customerId != null
                && this.customerId == rhs.customerId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "customerId=" + this.customerId +
            ", state=" + this.state +
            ", name=" + this.name +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"

    fun detachedCopy(): CustomerDO = CustomerDO(
        customerId = this.customerId,
        version = this.version,
        created = this.created,
        updated = this.updated,
        state = this.state,
        name = this.name.detachedCopy(),
        addressIdPool = this.addressIdPool,
    )

    @Embeddable
    open class NameDO(

        @Enumerated(EnumType.STRING)
        @Column(
            name = "TITLE",
            nullable = false,
            length = 16
        )
        open var title: Title? = null,

        @Column(
            name = "FIRST_NAME",
            nullable = false,
            length = 127,
        )
        open var firstName: String? = null,

        @Column(
            name = "LAST_NAME",
            nullable = false,
            length = 127,
        )
        open var lastName: String? = null,

        ) {

        override fun equals(other: Any?): Boolean {
            if (this === other)
                return true
            val rhs = other as? NameDO
            return rhs != null
                    && Objects.equals(this.title, rhs.title)
                    && Objects.equals(this.firstName, rhs.firstName)
                    && Objects.equals(this.lastName, rhs.lastName)
        }

        override fun hashCode(): Int =
            Objects.hash(this.title, this.firstName, this.lastName)

        override fun toString(): String = this.javaClass.simpleName + "(" +
                "title=" + this.title +
                ", firstName=" + this.firstName +
                ", lastName=" + this.lastName +
                ")"

        fun detachedCopy(): NameDO = NameDO(
            title = this.title,
            firstName = this.firstName,
            lastName = this.lastName,
        )

    }

}
