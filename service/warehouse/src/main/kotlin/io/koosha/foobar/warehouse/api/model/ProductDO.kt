@file:Suppress("unused")

package io.koosha.foobar.warehouse.api.model

import io.koosha.foobar.warehouse.API_PREFIX
import java.time.ZonedDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version


@Entity
@Table(name = "${API_PREFIX}__${ProductDO.ENTITY_TYPE}")
open class ProductDO(

    @Id
    @Column(
        name = "PRODUCT_ID",
        length = 36,
    )
    @org.hibernate.annotations.Type(type = "uuid-char")
    open var productId: UUID? = null,

    @Version
    @Column(name = "VERSION")
    open var version: Long? = null,

    @Column(
        name = "CREATED",
        nullable = false,
    )
    open var created: ZonedDateTime? = null,

    @Column(
        name = "UPDATED",
        nullable = false,
    )
    open var updated: ZonedDateTime? = null,

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

    companion object {
        const val ENTITY_TYPE = "product"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as ProductDO
        return this.productId != null
                && this.productId == rhs.productId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "productId=" + this.productId +
            ", version=" + this.version +
            ", created=" + this.created +
            ", updated=" + this.updated +
            ", name=" + this.name +
            ", unitSingle=" + this.unitSingle +
            ", unitMultiple=" + this.unitMultiple +
            ", enabled=" + this.active +
            ")"

}
