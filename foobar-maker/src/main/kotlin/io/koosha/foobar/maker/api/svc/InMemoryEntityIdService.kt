package io.koosha.foobar.maker.api.svc

import io.koosha.foobar.common.PROFILE__DISABLE_DB
import io.koosha.foobar.maker.api.CliException
import io.koosha.foobar.maker.api.model.EntityId
import mu.KotlinLogging
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write


@Service
@Profile(PROFILE__DISABLE_DB)
class InMemoryEntityIdService : EntityIdService {

    private val log = KotlinLogging.logger {}

    private val lock = ReentrantReadWriteLock()

    private val internalIdToExternalIdByEntityType = mutableMapOf<String, MutableMap<Long, String>>()
    private val lastInternalIdByEntityType = mutableMapOf<String, Long>()

    private val lineItemWorkQueue = mutableListOf<UUID>()
    private val updateWorkQueue = mutableListOf<UUID>()

    private val productIdToAvailability = mutableListOf<ProductAndAvailability>()

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
                this.lock.read {
                    val internalIdToExternalId = this.internalIdToExternalIdByEntityType[entityType]
                        ?: throw CliException("unknown entity type or no ids yet, entityType=$entityType")
                    val externalId = internalIdToExternalId[longId]
                        ?: throw CliException("entity id not found, entityType=$entityType, entityInternalId=$longId")
                    UUID.fromString(externalId)
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
            val latest = this.lock.read {
                this.lastInternalIdByEntityType[entityType]
                    ?: throw CliException("unknown entity type or no ids yet, entityType=$entityType")
            }
            val id = this.findUUID(entityType, latest.toString())
            log.trace { "mapped entityType=$entityType givenId=last($latest) to=$id" }
            id
        }
        else {
            this.findUUID(entityType, givenId)
        }

    override fun findInternalIdByEntityTypeAndEntityId(
        entityType: String,
        entityId: String,
    ): Optional<EntityId> = this.lock.read {
        val internalIdToExternalId: MutableMap<Long, String> = this.internalIdToExternalIdByEntityType[entityType]
            ?: throw CliException("unknown entity type or no ids yet, entityType=$entityType")
        internalIdToExternalId
            .entries
            .stream()
            .filter { it.value == entityId }
            .findFirst()
            .map {
                EntityId(entityId, it.key, entityType)
            }
    }

    override fun findEntityIdByEntityTypeAndInternalId(
        entityType: String,
        internalId: Long,
    ): Optional<EntityId> = this.lock.read {
        val internalIdToExternalId: MutableMap<Long, String> = this.internalIdToExternalIdByEntityType[entityType]
            ?: return Optional.empty()
        val externalId = internalIdToExternalId[internalId]
            ?: return Optional.empty()
        Optional.of(EntityId(entityType, internalId, externalId))
    }

    override fun findMaxInternalIdByEntityType(entityType: String): Optional<Long> = this.lock.read {
        Optional.ofNullable(this.lastInternalIdByEntityType[entityType])
    }

    override fun save(entityId: EntityId): EntityId =
        this.lock.write {
            val internalIdToExternalId: MutableMap<Long, String> =
                this.internalIdToExternalIdByEntityType.computeIfAbsent(entityId.entityType!!) {
                    mutableMapOf()
                }
            internalIdToExternalId[entityId.internalId!!] = entityId.entityId!!
            this.lastInternalIdByEntityType[entityId.entityType!!] = entityId.internalId!!
            entityId
        }

    override fun putOrderRequestIntoLineItemWorkQueue(orderRequestId: UUID): Unit = this.lock.write {
        this.lineItemWorkQueue += orderRequestId
    }

    override fun getOrderRequestFromLineItemWorkQueue(): Optional<UUID> = this.lock.read {
        if (this.lineItemWorkQueue.isEmpty())
            Optional.empty()
        else
            Optional.of(this.lineItemWorkQueue.removeLast())
    }

    override fun putOrderRequestIntoUpdateWorkQueue(orderRequestId: UUID) = this.lock.write {
        this.updateWorkQueue += orderRequestId
    }

    override fun getOrderRequestFromUpdateWorkQueue(): Optional<UUID> = this.lock.read {
        if (this.updateWorkQueue.isEmpty())
            Optional.empty()
        else
            Optional.of(this.updateWorkQueue.removeLast())
    }

    override fun getAvailableProduct(
        units: Long,
        excluding: Collection<UUID>,
    ): Optional<UUID> = this.lock.write {
        val find = this.productIdToAvailability
            .stream()
            .filter {
                !excluding.contains(it.productId) && it.units >= units
            }
            .findAny()
            .map {
                it.units -= units
                it.productId
            }

        if (find.isEmpty)
            log.warn("SO FAR: {} {}", productIdToAvailability.size, productIdToAvailability.firstOrNull())

        return find
    }

    override fun putAvailableProduct(
        units: Long,
        productId: UUID,
    ) = this.lock.write {
        this.productIdToAvailability += ProductAndAvailability(productId, units)
    }


    private data class ProductAndAvailability(
        val productId: UUID,
        var units: Long,
    )
}
