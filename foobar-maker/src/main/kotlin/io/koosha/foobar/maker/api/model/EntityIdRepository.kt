package io.koosha.foobar.maker.api.model

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface EntityIdRepository : JpaRepository<EntityId, UUID> {

    fun findInternalIdByEntityTypeAndEntityId(entityType: String, entityId: String): Optional<EntityId>

    fun findEntityIdByEntityTypeAndInternalId(entityType: String, internalId: Long): Optional<EntityId>

    @Query("SELECT MAX(internalId) FROM EntityId WHERE entityType = :entityType")
    fun findMaxInternalIdByEntityType(entityType: String): Optional<Long>

}
