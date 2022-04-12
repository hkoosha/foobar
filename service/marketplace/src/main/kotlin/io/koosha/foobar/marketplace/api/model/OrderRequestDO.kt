package io.koosha.foobar.marketplace.api.model

import io.koosha.foobar.marketplace.API_PREFIX
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table
import javax.persistence.Version


@Entity
@Table(name = "${API_PREFIX}__order_request")
open class OrderRequestDO(

    @Id
    @Column(
        name = "ORDER_REQUEST_ID",
        length = 36,
    )
    @org.hibernate.annotations.Type(type = "uuid-char")
    open var orderRequestId: UUID? = null,

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
        name = "LINE_ITEM_ID_POOL",
        nullable = false,
    )
    open var lineItemIdPool: Long? = null,

    @Column(
        name = "CUSTOMER_ID",
        nullable = true,
        length = 36,
    )
    @org.hibernate.annotations.Type(type = "uuid-char")
    open var customerId: UUID? = null,

    @Column(
        name = "SELLER_ID",
        nullable = true,
        length = 36,
    )
    @org.hibernate.annotations.Type(type = "uuid-char")
    open var sellerId: UUID? = null,

    @Enumerated(EnumType.STRING)
    @Column(
        name = "STATE",
        nullable = false,
        length = 32,
    )
    open var state: OrderRequestState? = null,

    @Column(
        name = "SUB_TOTAL",
        nullable = true,
    )
    open var subTotal: Long? = null,

    ) {

    companion object {
        const val ENTITY_TYPE = "order_request"
        const val ENTITY_TYPE_DASHED = "order-request"
    }

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
        val rhs = other as OrderRequestDO
        return this.orderRequestId != null
                && this.orderRequestId == rhs.orderRequestId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "orderRequestId=" + this.orderRequestId +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ", sellerId=" + this.sellerId +
            ", customerId=" + this.customerId +
            ", state=" + this.state +
            ", subTotal=" + this.subTotal +
            ")"

}
