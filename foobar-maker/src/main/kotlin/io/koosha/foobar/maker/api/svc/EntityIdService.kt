package io.koosha.foobar.maker.api.svc

import io.koosha.foobar.maker.api.model.EntityId
import java.util.*

interface EntityIdService {

    fun putOrderRequestIntoLineItemWorkQueue(orderRequestId: UUID)

    fun getOrderRequestFromLineItemWorkQueue(): Optional<UUID>

    fun putOrderRequestIntoUpdateWorkQueue(orderRequestId: UUID)

    fun getOrderRequestFromUpdateWorkQueue(): Optional<UUID>

    fun getAvailableProduct(units: Long): Optional<UUID> = this.getAvailableProduct(units, emptySet())

    fun getAvailableProduct(units: Long, excluding: Collection<UUID>): Optional<UUID>

    fun putAvailableProduct(
        units: Long,
        productId: UUID,
    )

    fun findUUID(
        entityType: String,
        givenId: String,
    ): UUID

    fun findUUIDOrLast(
        entityType: String,
        givenId: String?,
    ): UUID

    fun findInternalIdByEntityTypeAndEntityId(
        entityType: String,
        entityId: String,
    ): Optional<EntityId>

    fun findEntityIdByEntityTypeAndInternalId(
        entityType: String,
        internalId: Long,
    ): Optional<EntityId>

    fun findMaxInternalIdByEntityType(entityType: String): Optional<Long>

    fun save(entityId: EntityId): EntityId

}
