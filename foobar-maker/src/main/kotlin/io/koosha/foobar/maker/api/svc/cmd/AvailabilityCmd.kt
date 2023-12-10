package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.common.toUUID
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.connect.ProductAvailabilityApi
import io.koosha.foobar.maker.api.firstOrNull
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.svc.EntityIdService
import io.koosha.foobar.maker.api.svc.Rand
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AvailabilityCmd(
    private val rand: Rand,
    private val availabilityApi: ProductAvailabilityApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = LoggerFactory.getLogger(this::class.java)

    override val commandName: String = "availability"

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
            freeArgs.subList(1, freeArgs.size)
        )

        else -> log.error("unknown availability command: {}", freeArgs[0])
    }

    fun postAvailability(
        args: ApplicationArguments,
        freeArgs: List<String>,
        doLog: Boolean = true,
    ) {

        val sellerId: UUID = args.firstOrNull("seller-id")?.toUUID()
            ?: this.entityIdService.findUUIDOrLast("seller", freeArgs.firstOrNull())

        val productId: UUID = args.firstOrNull("product-id")?.toUUID()
            ?: this.entityIdService.findUUIDOrLast("product", freeArgs.firstOrNull())

        val req = ProductAvailabilityApi.CreateDto(
            sellerId = sellerId,
            unitsAvailable = args.firstOrNull("unitsAvailable")?.toLong() ?: this.rand.long(),
            pricePerUnit = args.firstOrNull("pricePerUnit")?.toLong()
                ?: this.rand.long(max = 100_000L, min = 10_000),
        )

        if (doLog)
            log.info("request:\n{}", req)
        val response = this.availabilityApi.create(productId, req)

        val internalId = this.entityIdService
            .findMaxInternalIdByEntityType("availability")
            .map { it + 1 }
            .orElse(0L)

        this.entityIdService.save(
            EntityId(
                entityId = "$productId/$sellerId",
                internalId = internalId,
                entityType = "availability",
            )
        )

        this.entityIdService.putAvailableProduct(response.unitsAvailable, productId)

        if (doLog)
            log.info("posted availability:\n{}", response)
    }

    fun getAvailability(
        freeArgs: List<String>,
    ) {

        val productId: UUID = this.entityIdService.findUUIDOrLast("product", freeArgs.firstOrNull())

        if (freeArgs.size < 2) {

            val response = this.availabilityApi.readAll(productId)
            this.log.info("product availabilities:\n{}", response)

        }
        else {

            val sellerId: UUID = this.entityIdService.findUUIDOrLast("seller", freeArgs[1])
            val response = this.availabilityApi.read(productId, sellerId)
            log.info("product availability:\n{}", response)

        }
    }

}
