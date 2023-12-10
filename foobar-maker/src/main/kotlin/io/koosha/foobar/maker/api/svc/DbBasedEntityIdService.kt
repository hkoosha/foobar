package io.koosha.foobar.maker.api.svc

import io.koosha.foobar.common.PROFILE__DISABLE_DB
import io.koosha.foobar.maker.api.CliException
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.model.EntityIdRepository
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.Optional
import java.util.UUID

@Service
@Profile("!$PROFILE__DISABLE_DB")
class DbBasedEntityIdService(
    private val repo: EntityIdRepository,
) : EntityIdService {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun putOrderRequestIntoLineItemWorkQueue(orderRequestId: UUID) {
        log.warn(
            "!!!operation ignored!!! it is not implemented -> " +
                    "putOrderRequestIntoLineItemWorkQueue({})",
            orderRequestId
        )
    }

    override fun getOrderRequestFromLineItemWorkQueue(): Optional<UUID> = TODO("Not yet implemented")

    override fun putOrderRequestIntoUpdateWorkQueue(orderRequestId: UUID) {
        log.warn(
            "!!!operation ignored!!! it is not implemented -> putOrderRequestIntoUpdateWorkQueue({})",
            orderRequestId
        )
    }

    override fun getOrderRequestFromUpdateWorkQueue(): Optional<UUID> = TODO("Not yet implemented")

    override fun getAvailableProduct(units: Long, excluding: Collection<UUID>): Optional<UUID> =
        TODO("Not yet implemented")

    override fun putAvailableProduct(units: Long, productId: UUID) {
        log.warn(
            "!!!operation ignored!!! it is not implemented -> putAvailableProduct({}, {})",
            units, productId
        )
    }

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

        log.trace("mapped entityType={} givenId={} to={}", entityType, givenId, id)

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
            log.trace("mapped entityType={} givenId=last({}) to={}", entityType, latest, id)
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
