package io.koosha.foobar.marketplace.api.model.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "marketplace__order_request_line_item")
open class OrderRequestLineItemDO(

    @Id
    open var internalOrderRequestLineItemId: String? = null,

    open var orderRequestLineItemId: Long? = null,

    open var orderRequestId: String? = null,

    @Version
    open var version: Long? = null,

    @CreatedDate
    open var created: Instant? = null,

    @LastModifiedDate
    open var updated: Instant? = null,

    open var productId: String? = null,

    open var units: Long? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        val rhs = other as? OrderRequestLineItemDO
        return this.internalOrderRequestLineItemId != null
                && rhs != null
                && this.internalOrderRequestLineItemId == rhs.internalOrderRequestLineItemId
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
