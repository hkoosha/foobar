@file:Suppress("unused")

package io.koosha.foobar.seller.api.model

import io.koosha.foobar.seller.API_PREFIX
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
@Table(name = "${API_PREFIX}__${SellerDO.ENTITY_TYPE}")
@EntityListeners(AuditingEntityListener::class)
open class SellerDO(

    @Id
    @Column(
        name = "SELLER_ID",
        length = 36,
    )
    @org.hibernate.annotations.Type(type = "uuid-char")
    open var sellerId: UUID? = null,

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

    companion object {
        const val ENTITY_TYPE = "seller"
    }

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
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as SellerDO
        return this.sellerId != null
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
