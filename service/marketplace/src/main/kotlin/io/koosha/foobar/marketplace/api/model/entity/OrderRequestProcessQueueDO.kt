package io.koosha.foobar.marketplace.api.model.entity

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.time.LocalDateTime

@Table(name = "marketplace__order_request_process_queue")
open class OrderRequestProcessQueueDO(

    @Id
    open var orderRequestId: String? = null,

    @Version
    open var version: Long? = null,

    @CreatedDate
    open var created: Instant? = null,

    @LastModifiedDate
    open var updated: Instant? = null,

    open var synced: Boolean? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        val rhs = other as? OrderRequestProcessQueueDO
        return this.orderRequestId != null
                && rhs != null
                && this.orderRequestId == rhs.orderRequestId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "orderRequestId=" + this.orderRequestId +
            ", synced=" + this.synced +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"

}
