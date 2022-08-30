package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.connect.seller.generated.api.SellerApi
import io.koosha.foobar.connect.seller.generated.api.SellerCreateRequest
import io.koosha.foobar.connect.seller.generated.api.SellerCreateRequestAddress
import io.koosha.foobar.connect.seller.generated.api.SellerUpdateRequest
import io.koosha.foobar.connect.seller.generated.api.SellerUpdateRequestAddress
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.assertStatusCode
import io.koosha.foobar.maker.api.firstOrNull
import io.koosha.foobar.maker.api.firstOrRandom
import io.koosha.foobar.maker.api.firstOrRandomUnPrefixed
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.model.EntityIdRepository
import io.koosha.foobar.maker.api.svc.EntityIdService
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component


@Component
class SellerCmd(
    private val sellerApi: SellerApi,
    private val entityIdService: EntityIdService,
    private val repo: EntityIdRepository,
) : Command {

    private val log = KotlinLogging.logger {}

    override val commandName: String = SellerApi.ENTITY_TYPE

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

        else -> log.error { "unknown seller command: ${freeArgs[0]}" }
    }

    fun postSeller(
        args: ApplicationArguments,
    ) {

        val req = SellerCreateRequest()
        req.name = args.firstOrRandom("name")
        req.address = SellerCreateRequestAddress()
        req.address.addressLine1 = args.firstOrRandom("addressLine1")
        req.address.city = args.firstOrRandom("city")
        req.address.country = args.firstOrRandom("country")
        req.address.zipcode = args.firstOrRandomUnPrefixed("zipcode")

        log.info { "request:\n$req" }
        val response = this.sellerApi.postSellerWithHttpInfo(req)
        assertStatusCode(response.statusCode)
        val entity = response.data

        val internalId = this.repo
            .findMaxInternalIdByEntityType(SellerApi.ENTITY_TYPE)
            .map { it + 1 }
            .orElse(0L)
        this.repo.save(
            EntityId(
                entityId = entity.sellerId.toString(),
                internalId = internalId,
                entityType = SellerApi.ENTITY_TYPE,
            )
        )

        log.info { "posted seller:\n${response.headers}\ninternalId=$internalId\nentity:\n$entity" }
    }

    fun patchSeller(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val sellerId = this.entityIdService.findUUIDOrLast(SellerApi.ENTITY_TYPE, freeArgs.firstOrNull())

        val req = SellerUpdateRequest()
        req.name = args.firstOrNull("name")
        req.address = SellerUpdateRequestAddress()
        req.address!!.addressLine1 = args.firstOrNull("addressLine1")
        req.address!!.city = args.firstOrNull("city")
        req.address!!.country = args.firstOrNull("country")
        req.address!!.zipcode = args.firstOrNull("zipcode")

        log.info { "request:\n$req" }
        val response = this.sellerApi.patchSellerWithHttpInfo(sellerId, req)
        assertStatusCode(response.statusCode)
        val entity = response.data
        log.info { "patched seller:\n${response.headers}\n$entity" }
    }

    fun getSeller(
        freeArgs: List<String>,
    ) = if (freeArgs.isEmpty()) {

        val response = this.sellerApi.sellersWithHttpInfo
        assertStatusCode(response.statusCode)
        val all = response.data
        this.log.info { "sellers:\n${response.headers}\n$all" }

    }
    else {

        val sellerId = this.entityIdService.findUUID(SellerApi.ENTITY_TYPE, freeArgs.first())

        val response = this.sellerApi.getSellerWithHttpInfo(sellerId)
        assertStatusCode(response.statusCode)
        val entity = response.data

        log.info { "seller:\n${response.headers}\n$entity" }

    }

}
