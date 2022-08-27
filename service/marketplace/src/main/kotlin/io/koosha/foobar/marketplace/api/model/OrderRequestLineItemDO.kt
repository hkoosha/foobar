package io.koosha.foobar.marketplace.api.model

import io.koosha.foobar.marketplace.API_PREFIX
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime


@Table(name = "${API_PREFIX}__${OrderRequestLineItemDO.ENTITY_TYPE}")
open class OrderRequestLineItemDO(

    @Id
    open var internalOrderRequestLineItemId: String? = null,

    open var orderRequestLineItemId: Long? = null,

    open var orderRequestId: String? = null,

    @Version
    open var version: Long? = null,

    @CreatedDate
    open var created: LocalDateTime? = null,

    @LastModifiedDate
    open var updated: LocalDateTime? = null,

    open var productId: String? = null,

    open var units: Long? = null,

    ) {

    companion object {
        const val ENTITY_TYPE = "order_request_line_item"
        const val ENTITY_TYPE_DASHED = "order-request-line-item"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as OrderRequestLineItemDO
        return this.internalOrderRequestLineItemId != null &&
                this.internalOrderRequestLineItemId == rhs.internalOrderRequestLineItemId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "internalOrderRequestLineItemId=" + this.internalOrderRequestLineItemId +
            ", orderRequestId=" + this.orderRequestId +
            ", orderRequestLineItemId=" + this.orderRequestLineItemId +
            ", productId=" + this.productId +
            ", units=" + this.units +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"

}
