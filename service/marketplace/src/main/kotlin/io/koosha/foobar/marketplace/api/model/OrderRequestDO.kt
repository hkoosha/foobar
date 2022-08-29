package io.koosha.foobar.marketplace.api.model

import io.koosha.foobar.marketplace.API_PREFIX
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime


@Table(name = "${API_PREFIX}__order_request")
open class OrderRequestDO(

    @Id
    open var orderRequestId: String? = null,

    @Version
    open var version: Long? = null,

    @CreatedDate
    open var created: LocalDateTime? = null,

    @LastModifiedDate
    open var updated: LocalDateTime? = null,

    open var lineItemIdPool: Long? = null,

    open var customerId: String? = null,

    open var sellerId: String? = null,

    open var state: OrderRequestState? = null,

    open var subTotal: Long? = null,

    ) {

    companion object {
        const val ENTITY_TYPE = "order_request"
        const val ENTITY_TYPE_DASHED = "order-request"
    }

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
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as OrderRequestDO
        return this.orderRequestId != null
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
