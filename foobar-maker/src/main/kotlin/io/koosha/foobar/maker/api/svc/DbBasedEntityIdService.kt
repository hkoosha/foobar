package io.koosha.foobar.maker.api.svc

import io.koosha.foobar.common.PROFILE__DISABLE_DB
import io.koosha.foobar.maker.api.CliException
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.model.EntityIdRepository
import mu.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.*


@Service
@Profile("!$PROFILE__DISABLE_DB")
class DbBasedEntityIdService(
    private val repo: EntityIdRepository,
) : EntityIdService {

    private val log = KotlinLogging.logger {}

    override fun putOrderRequestIntoLineItemWorkQueue(orderRequestId: UUID): Unit = TODO("Not yet implemented")

    override fun getOrderRequestFromLineItemWorkQueue(): Optional<UUID> = TODO("Not yet implemented")

    override fun putOrderRequestIntoUpdateWorkQueue(orderRequestId: UUID): Unit = TODO("Not yet implemented")

    override fun getOrderRequestFromUpdateWorkQueue(): Optional<UUID> = TODO("Not yet implemented")

    override fun getAvailableProduct(units: Long, excluding: Collection<UUID>): Optional<UUID> =
        TODO("Not yet implemented")

    override fun putAvailableProduct(units: Long, productId: UUID): Unit = TODO("Not yet implemented")

    override fun findUUID(
        entityType: String,
        givenId: String,
    ): UUID {

        val longId = try {
            givenId.toLong()
        }
        catch (e: NumberFormatException) {
            null
        }

        val id: UUID =
            if (longId != null)
                this.findEntityIdByEntityTypeAndInternalId(entityType, longId)
                    .map { it.entityId!! }
                    .map(UUID::fromString)
                    .orElseThrow {
                        CliException("entity id not found, entityType=$entityType, entityInternalId=$longId")
                    }
            else
                try {
                    UUID.fromString(givenId)
                }
                catch (@Suppress("SwallowedException") ex: IllegalArgumentException) {
                    throw CliException("invalid id, neither valid UUID nor a valid long, id=$givenId")
                }

        log.trace { "mapped entityType=$entityType givenId=$givenId to=$id" }

        return id
    }

    override fun findUUIDOrLast(
        entityType: String,
        givenId: String?,
    ): UUID =
        if (givenId == null || givenId == "last") {
            val latest = repo
                .findMaxInternalIdByEntityType(entityType)
                .orElseThrow {
                    CliException("no id found for entityType=$entityType")
                }
                .toString()
            val id = this.findUUID(entityType, latest)
            log.trace { "mapped entityType=$entityType givenId=last($latest) to=$id" }
            id
        }
        else {
            this.findUUID(entityType, givenId)
        }

    override fun findInternalIdByEntityTypeAndEntityId(
        entityType: String,
        entityId: String,
    ): Optional<EntityId> = this.repo.findInternalIdByEntityTypeAndEntityId(entityType, entityId)

    override fun findEntityIdByEntityTypeAndInternalId(
        entityType: String,
        internalId: Long,
    ): Optional<EntityId> = this.repo.findEntityIdByEntityTypeAndInternalId(entityType, internalId)

    override fun findMaxInternalIdByEntityType(entityType: String): Optional<Long> =
        this.repo.findMaxInternalIdByEntityType(entityType)

    override fun save(entityId: EntityId) = this.repo.save(entityId)

}
