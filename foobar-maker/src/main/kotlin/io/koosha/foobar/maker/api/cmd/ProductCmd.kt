package io.koosha.foobar.maker.api.cmd

import io.koosha.foobar.connect.warehouse.generated.api.ProductApi
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.assertStatusCode
import io.koosha.foobar.maker.api.firstOrDef
import io.koosha.foobar.maker.api.firstOrNull
import io.koosha.foobar.maker.api.firstOrRandom
import io.koosha.foobar.maker.api.firstOrRandomUnPrefixed
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.model.EntityIdRepository
import io.koosha.foobar.maker.api.svc.EntityIdService
import mu.KotlinLogging
import org.openapitools.client.model.ProductCreateRequest
import org.openapitools.client.model.ProductUpdateRequest
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component


@Component
class ProductCmd(
    private val productApi: ProductApi,
    private val entityIdService: EntityIdService,
    private val repo: EntityIdRepository,
) : Command {

    private val log = KotlinLogging.logger {}

    override val commandName: String = ProductApi.ENTITY_TYPE

    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) = when {
        freeArgs.isEmpty() -> this.postProduct(
            args,
        )

        matches("post", freeArgs[0]) -> this.postProduct(
            args,
        )

        matches("patch", freeArgs[0]) -> this.patchProduct(
            args,
            freeArgs.subList(1, freeArgs.size)
        )

        matches("get", freeArgs[0]) -> this.getProduct(
            freeArgs.subList(1, freeArgs.size)
        )

        else -> log.error { "unknown product command: ${freeArgs[0]}" }
    }

    fun postProduct(
        args: ApplicationArguments,
    ) {

        val req = ProductCreateRequest()
        req.name = args.firstOrRandom("name")
        req.active = args.firstOrDef("active", "true").toBooleanStrict()
        req.unitSingle = args.firstOrRandomUnPrefixed("unitSingle")
        req.unitMultiple = args.firstOrRandomUnPrefixed("unitMultiple")

        log.info { "request:\n$req" }
        val response = this.productApi.postProductWithHttpInfo(req)
        assertStatusCode(response.statusCode)
        val entity = response.data

        val internalId = this.repo
            .findMaxInternalIdByEntityType(ProductApi.ENTITY_TYPE)
            .map { it + 1 }
            .orElse(0L)
        this.repo.save(
            EntityId(
                entityId = entity.productId.toString(),
                internalId = internalId,
                entityType = ProductApi.ENTITY_TYPE,
            )
        )

        log.info { "posted product:\n${response.headers}\ninternalId=$internalId\nentity:\n$entity" }
    }

    fun patchProduct(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val productId = this.entityIdService.findUUIDOrLast(ProductApi.ENTITY_TYPE, freeArgs.firstOrNull())

        val req = ProductUpdateRequest()
        req.name = args.firstOrNull("name")
        req.active = args.firstOrNull("active")?.toBooleanStrict()
        req.unitSingle = args.firstOrNull("unitSingle")
        req.unitMultiple = args.firstOrNull("unitMultiple")

        log.info { "request:\n$req" }
        val response = this.productApi.patchProductWithHttpInfo(productId, req)
        assertStatusCode(response.statusCode)
        val entity = response.data
        log.info { "patched product:\n${response.headers}\n$entity" }
    }

    fun getProduct(
        freeArgs: List<String>,
    ) = if (freeArgs.isEmpty()) {

        val response = this.productApi.productsWithHttpInfo
        assertStatusCode(response.statusCode)
        val all = response.data
        this.log.info { "products:\n${response.statusCode}\n$all" }

    }
    else {

        val productId = this.entityIdService.findUUID(ProductApi.ENTITY_TYPE, freeArgs.first())

        val response = this.productApi.getProductWithHttpInfo(productId)
        assertStatusCode(response.statusCode)
        val entity = response.data

        log.info { "product:\n${response.headers}\n$entity" }

    }

}
