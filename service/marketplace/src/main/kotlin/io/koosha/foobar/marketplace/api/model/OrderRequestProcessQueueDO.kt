package io.koosha.foobar.marketplace.api.model


import io.koosha.foobar.marketplace.API_PREFIX
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import java.util.*
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EntityListeners
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table
import javax.persistence.Version


@Entity
@Table(name = "${API_PREFIX}__order_request_process_queue")
@EntityListeners(AuditingEntityListener::class)
open class OrderRequestProcessQueueDO(

    @Id
    @Column(
        name = "ORDER_REQUEST_PROCESS_QUEUE_ID",
        length = 36,
        nullable = false,
    )
    @org.hibernate.annotations.Type(type = "uuid-char")
    open var orderRequestProcessQueueId: UUID? = null,

    @OneToOne(
        fetch = FetchType.LAZY,
        cascade = [CascadeType.DETACH]
    )
    @JoinColumn(
        name = "ORDER_REQUEST_ID",
        nullable = false
    )
    @MapsId
    open var orderRequest: OrderRequestDO? = null,

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
        name = "SYNCED",
        nullable = false,
    )
    open var synced: Boolean? = null,

    ) : Serializable {

    companion object {
        private const val serialVersionUID = 0L
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as OrderRequestProcessQueueDO
        return this.orderRequestProcessQueueId != null
                && this.orderRequestProcessQueueId == rhs.orderRequestProcessQueueId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "orderRequestProcessQueueId=" + this.orderRequestProcessQueueId +
            ", synced=" + this.synced +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"

}
