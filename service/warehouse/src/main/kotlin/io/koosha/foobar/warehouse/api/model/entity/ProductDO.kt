@file:Suppress("unused")

package io.koosha.foobar.warehouse.api.model.entity

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
@Table(name = "warehouse__product")
@EntityListeners(AuditingEntityListener::class)
open class ProductDO(
    @Id
    @Column(
        name = "PRODUCT_ID",
        length = 36,
    )
    // @org.hibernate.annotations.Type(type = "uuid-char")
    open var productId: UUID? = null,

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
        name = "NAME",
        nullable = false,
    )
    open var name: String? = null,

    @Column(
        name = "UNIT_SINGLE",
        nullable = false,
    )
    open var unitSingle: String? = null,

    @Column(
        name = "UNIT_MULTIPLE",
        nullable = false,
    )
    open var unitMultiple: String? = null,

    @Column(
        name = "ACTIVE",
        nullable = false,
    )
    open var active: Boolean? = null,
) {

    fun detachedCopy(): ProductDO = ProductDO(
        productId = this.productId,
        version = this.version,
        created = this.created,
        updated = this.updated,
        name = this.name,
        unitSingle = this.unitSingle,
        unitMultiple = this.unitMultiple,
        active = this.active,
    )

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        val rhs = other as? ProductDO
        return rhs != null
                && this.productId != null
                && this.productId == rhs.productId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "productId=" + this.productId +
            ", name=" + this.name +
            ", unitSingle=" + this.unitSingle +
            ", unitMultiple=" + this.unitMultiple +
            ", enabled=" + this.active +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ")"

}
