package io.koosha.foobar.marketplace.api.model

import io.koosha.foobar.marketplace.API_PREFIX
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.io.Serializable
import java.time.LocalDateTime


@Table(name = "${API_PREFIX}__processed_order_request_seller")
open class ProcessedOrderRequestSellerDO(

    @Id
    open var orderRequestId: String? = null,

    @Version
    open var version: Long? = null,

    @CreatedDate
    open var created: LocalDateTime? = null,

    @LastModifiedDate
    open var updated: LocalDateTime? = null,

    ) : Serializable {

    companion object {
        private const val serialVersionUID = 0L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as ProcessedOrderRequestSellerDO
        return this.orderRequestId != null
                && this.orderRequestId == rhs.orderRequestId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "orderRequestId=" + this.orderRequestId +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"

}
