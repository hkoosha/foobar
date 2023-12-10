package io.koosha.foobar.marketplace.api.model.entity

import io.koosha.foobar.marketplace.api.model.OrderRequestState
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(name = "marketplace__order_request")
open class OrderRequestDO(

    @Id
    open var orderRequestId: String? = null,

    @Version
    open var version: Long? = null,

    @CreatedDate
    open var created: Instant? = null,

    @LastModifiedDate
    open var updated: Instant? = null,

    open var lineItemIdPool: Long? = null,

    open var customerId: String? = null,

    open var sellerId: String? = null,

    open var state: OrderRequestState? = null,

    open var subTotal: Long? = null,
) {

    fun detachedCopy(): OrderRequestDO = OrderRequestDO(
        orderRequestId = this.orderRequestId,
        created = this.created,
        updated = this.updated,
        lineItemIdPool = this.lineItemIdPool,
        customerId = this.customerId,
        sellerId = this.sellerId,
        state = this.state,
        subTotal = this.subTotal,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        val rhs = other as? OrderRequestDO
        return this.orderRequestId != null
                && rhs != null
                && this.orderRequestId == rhs.orderRequestId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "orderRequestId=" + this.orderRequestId +
            ", sellerId=" + this.sellerId +
            ", customerId=" + this.customerId +
            ", state=" + this.state +
            ", subTotal=" + this.subTotal +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"

}
