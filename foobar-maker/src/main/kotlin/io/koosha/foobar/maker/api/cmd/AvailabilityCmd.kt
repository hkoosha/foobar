package io.koosha.foobar.maker.api.cmd

import io.koosha.foobar.connect.customer.generated.api.AddressApi
import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.connect.warehouse.generated.api.AvailabilityApi
import io.koosha.foobar.connect.warehouse.generated.api.ProductApi
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.assertStatusCode
import io.koosha.foobar.maker.api.firstOrNull
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.model.EntityIdRepository
import io.koosha.foobar.maker.api.svc.EntityIdService
import io.koosha.foobar.maker.api.svc.Rand
import mu.KotlinLogging
import org.openapitools.client.model.AvailabilityCreateRequest
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.util.*


@Component
class AvailabilityCmd(
    private val rand: Rand,
    private val availabilityApi: AvailabilityApi,
    private val entityIdService: EntityIdService,
    private val repo: EntityIdRepository,
) : Command {

    private val log = KotlinLogging.logger {}

    override val commandName: String = AvailabilityApi.ENTITY_TYPE

    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) = when {
        freeArgs.isEmpty() -> this.postAvailability(
            args,
            freeArgs,
        )

        matches("post", freeArgs[0]) -> this.postAvailability(
            args,
            freeArgs.subList(1, freeArgs.size)
        )

        matches("get", freeArgs[0]) -> this.getAvailability(
            args,
            freeArgs.subList(1, freeArgs.size)
        )

        else -> log.error { "unknown availability command: ${freeArgs[0]}" }
    }

    fun postAvailability(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val sellerId: UUID = this.entityIdService.findUUIDOrLast(SellerApi.ENTITY_TYPE, freeArgs.firstOrNull())
        val productId: UUID = this.entityIdService.findUUIDOrLast(ProductApi.ENTITY_TYPE, freeArgs.firstOrNull())

        val req = AvailabilityCreateRequest()
        req.sellerId = sellerId
        req.unitsAvailable = args.firstOrNull("unitsAvailable")?.toLong() ?: this.rand.long()
        req.pricePerUnit = args.firstOrNull("pricePerUnit")?.toLong() ?: this.rand.long(max = 100_000L, min = 10_000)

        log.info { "request:\n$req" }
        val response = this.availabilityApi.postAvailabilityWithHttpInfo(productId, req)
        assertStatusCode(response.statusCode)
        val entity = response.data

        val internalId = this.repo
            .findMaxInternalIdByEntityType(AvailabilityApi.ENTITY_TYPE)
            .map { it + 1 }
            .orElse(0L)
        this.repo.save(
            EntityId(
                entityId = "$productId/$sellerId",
                internalId = internalId,
                entityType = AddressApi.ENTITY_TYPE,
            )
        )

        log.info { "posted availability:\n${response.headers}\n$entity" }
    }

    fun getAvailability(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val productId: UUID = this.entityIdService.findUUIDOrLast(ProductApi.ENTITY_TYPE, freeArgs.firstOrNull())

        if (freeArgs.size < 2) {

            val response = this.availabilityApi.getAvailabilitiesWithHttpInfo(productId)
            assertStatusCode(response.statusCode)
            val all = response.data
            this.log.info { "product availabilities:\n${response.headers}\n$all" }

        }
        else {

            val sellerId: UUID = this.entityIdService.findUUIDOrLast("seller", freeArgs[1])
            val response = this.availabilityApi.getAvailabilityWithHttpInfo(productId, sellerId)
            assertStatusCode(response.statusCode)
            val entity = response.data
            log.info { "product availability:\n${response.headers}\n$entity" }

        }
    }

}