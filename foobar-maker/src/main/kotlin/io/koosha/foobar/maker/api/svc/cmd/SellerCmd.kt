package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.connect.SellerApi
import io.koosha.foobar.maker.api.firstOrNull
import io.koosha.foobar.maker.api.firstOrRandom
import io.koosha.foobar.maker.api.firstOrRandomUnPrefixed
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.svc.EntityIdService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component

@Component
class SellerCmd(
    private val sellerApi: SellerApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = LoggerFactory.getLogger(this::class.java)

    override val commandName: String = "seller"

    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) = when {
        freeArgs.isEmpty() -> this.postSeller(
            args,
        )

        matches("post", freeArgs[0]) -> this.postSeller(
            args,
        )

        matches("patch", freeArgs[0]) -> this.patchSeller(
            args,
            freeArgs.subList(1, freeArgs.size)
        )

        matches("get", freeArgs[0]) -> this.getSeller(
            freeArgs.subList(1, freeArgs.size)
        )

        matches("last", freeArgs[0]) -> {
            this.getLastSeller(true)
            Unit
        }

        else -> log.error("unknown seller command: {}", freeArgs[0])
    }

    fun postSeller(
        args: ApplicationArguments,
        doLog: Boolean = true,
    ) {

        val req = SellerApi.CreateDto(
            name = args.firstOrRandom("name"),
            address = SellerApi.AddressDto(
                addressLine1 = args.firstOrRandom("addressLine1"),
                city = args.firstOrRandom("city"),
                country = args.firstOrRandom("country"),
                zipcode = args.firstOrRandomUnPrefixed("zipcode"),
            )
        )

        if (doLog)
            log.info("request:\n{}", req)

        val response = this.sellerApi.create(req)

        val internalId =
            this.entityIdService
                .findMaxInternalIdByEntityType("seller")
                .map { it + 1 }
                .orElse(0L)

        this.entityIdService.save(
            EntityId(
                entityId = response.sellerId.toString(),
                internalId = internalId,
                entityType = "seller",
            )
        )

        if (doLog)
            log.info("posted seller:\ninternalId={}\nentity:\n{}", internalId, response)
    }

    fun patchSeller(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val sellerId = this.entityIdService.findUUIDOrLast("seller", freeArgs.firstOrNull())

        val req = SellerApi.UpdateDto(
            name = args.firstOrNull("name"),
            address = SellerApi.AddressUpdateDto(
                addressLine1 = args.firstOrNull("addressLine1"),
                city = args.firstOrNull("city"),
                country = args.firstOrNull("country"),
                zipcode = args.firstOrNull("zipcode"),
            ),
        )

        log.info("request:\n{}", {})

        val response = this.sellerApi.update(sellerId, req)

        log.info("patched seller:\n{}", response)
    }

    fun getSeller(
        freeArgs: List<String>,
    ) = if (freeArgs.isEmpty()) {

        val response = this.sellerApi.readAll()
        log.info("sellers:\n{}", response)

    }
    else {

        val sellerId = this.entityIdService.findUUID("seller", freeArgs.first())

        val response = this.sellerApi.read(sellerId)

        log.info("seller:\n{}", response)

    }

    fun getLastSeller(doLog: Boolean): SellerApi.ReadDto {

        val sellerId = this.entityIdService.findUUIDOrLast("seller", null)
        val response = this.sellerApi.read(sellerId)

        if (doLog)
            log.info("seller:\n{}", response)

        return response
    }

}
