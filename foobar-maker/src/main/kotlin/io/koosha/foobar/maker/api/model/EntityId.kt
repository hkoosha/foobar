package io.koosha.foobar.maker.api.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table


@Entity
@Table(name = "maker__entity_id")
open class EntityId(

    @Id
    @Column(name = "ENTITY_ID")
    open var entityId: String? = null,

    @Column(name = "INTERNAL_ID")
    open var internalId: Long? = null,

    @Column(
        name = "ENTITY_TYPE",
        nullable = false,
        length = 32,
    )
    open var entityType: String? = null,

    ) {

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (this.javaClass != other?.javaClass)
            return false
        val rhs = other as EntityId
        return this.entityId != null
                && this.entityId == rhs.entityId
    }

    override fun hashCode(): Int = this.javaClass.hashCode()

    override fun toString(): String = this.javaClass.simpleName + "(" +
            "entityId=" + this.entityId +
            ", entityType=" + this.entityType +
            ", internalId=" + this.internalId +
            ")"

}
