package io.koosha.foobar.marketplaceengine.api.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "marketplace_engine__processed_uuid")
@EntityListeners(AuditingEntityListener::class)
open class ProcessedOrderRequestDO(
    @Id
    @Column(
        name = "ORDER_REQUEST_ID",
        nullable = false,
        length = 36,
    )
    // @org.hibernate.annotations.Type(type = "uuid-char")
    open var orderRequestId: UUID? = null,

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

    @Column(
        name = "PROCESSED",
        nullable = false,
    )
    open var processed: Boolean? = null,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        val rhs = other as? ProcessedOrderRequestDO
        return rhs != null
                && this.orderRequestId != null
                && this.orderRequestId == rhs.orderRequestId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "orderRequestId=" + this.orderRequestId +
            ", processed=" + this.processed +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"

}
