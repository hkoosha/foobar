package io.koosha.foobar.marketplace.api.model

import io.koosha.foobar.marketplace.API_PREFIX
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
import javax.persistence.ManyToOne
import javax.persistence.PrePersist
import javax.persistence.PreUpdate
import javax.persistence.Table
import javax.persistence.Version


@Entity
@Table(name = "${API_PREFIX}__${OrderRequestLineItemDO.ENTITY_TYPE}")
open class OrderRequestLineItemDO(

    @EmbeddedId
    open var orderRequestLineItemPk: Pk = Pk(),

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
        name = "PRODUCT_ID",
        length = 36,
        nullable = false,
    )
    @org.hibernate.annotations.Type(type = "uuid-char")
    open var productId: UUID? = null,

    @Column(
        name = "UNITS",
        nullable = false,
    )
    open var units: Long? = null,

    ) {

    companion object {
        const val ENTITY_TYPE = "order_request_line_item"
        const val ENTITY_TYPE_DASHED = "order-request-line-item"
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
        val rhs = other as OrderRequestLineItemDO
        return this.orderRequestLineItemPk == rhs.orderRequestLineItemPk
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName +
            "(orderRequestId" + this.orderRequestLineItemPk.orderRequest?.orderRequestId +
            " orderRequestLineItemId=" + this.orderRequestLineItemPk.orderRequestLineItemId +
            ")"


    @Embeddable
    open class Pk(

        @Column(
            name = "ORDER_REQUEST_LINE_ITEM_ID",
            nullable = false,
        )
        open var orderRequestLineItemId: Long? = null,

        @ManyToOne(optional = false)
        open var orderRequest: OrderRequestDO? = null,

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
            return this.orderRequestLineItemId != null
                    && this.orderRequest?.orderRequestId != null
                    && this.orderRequestLineItemId == rhs.orderRequestLineItemId
                    && this.orderRequest?.orderRequestId == rhs.orderRequest?.orderRequestId
        }

        @Suppress("RedundantOverride")
        override fun hashCode(): Int = super.hashCode()

        override fun toString(): String = this.javaClass.simpleName + "(" +
                "orderRequestId=" + this.orderRequest?.orderRequestId +
                "orderRequestLineItemId=" + this.orderRequestLineItemId +
                ")"

    }

}
