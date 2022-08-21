package io.koosha.foobar.maker.api.svc

import io.koosha.foobar.maker.api.CliException
import io.koosha.foobar.maker.api.model.EntityIdRepository
import mu.KotlinLogging
import org.springframework.stereotype.Component
import java.util.*


@Component
class EntityIdService(
    private val repo: EntityIdRepository,
) {

    private val log = KotlinLogging.logger {}

    fun findUUID(
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
                this.repo.findEntityIdByEntityTypeAndInternalId(entityType, longId)
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

        log.info { "mapped entityType=$entityType givenId=$givenId to=$id" }

        return id
    }

    fun findUUIDOrLast(
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
            val id = findUUID(entityType, latest)
            log.info { "mapped entityType=$entityType givenId=last($latest) to=$id" }
            id
        }
        else {
            findUUID(entityType, givenId)
        }

}
