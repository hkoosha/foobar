package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.connect.ProductApi
import io.koosha.foobar.maker.api.firstOrDef
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
class ProductCmd(
    private val productApi: ProductApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = LoggerFactory.getLogger(this::class.java)

    override val commandName: String = "product"

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

        matches("last", freeArgs[0]) -> {
            this.getLastProduct(true)
            Unit
        }

        else -> log.error("unknown product command: {}", freeArgs[0])
    }

    fun postProduct(
        args: ApplicationArguments,
        doLog: Boolean = true,
    ) {

        val req = ProductApi.CreateDto(
            name = args.firstOrRandom("name"),
            active = args.firstOrDef("active", "true").toBooleanStrict(),
            unitSingle = args.firstOrRandomUnPrefixed("unitSingle"),
            unitMultiple = args.firstOrRandomUnPrefixed("unitMultiple"),
        )

        if (doLog)
            log.info("request:\n{}", req)

        val response = this.productApi.create(req)

        val internalId =
            this.entityIdService
                .findMaxInternalIdByEntityType("product")
                .map { it + 1 }
                .orElse(0L)

        this.entityIdService.save(
            EntityId(
                entityId = response.productId.toString(),
                internalId = internalId,
                entityType = "product",
            )
        )

        if (doLog)
            log.info("posted product:\ninternalId={}\nentity:\n{}", internalId, response)
    }

    fun patchProduct(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val productId = this.entityIdService.findUUIDOrLast("product", freeArgs.firstOrNull())

        val req = ProductApi.UpdateDto(
            name = args.firstOrNull("name"),
            active = args.firstOrNull("active")?.toBooleanStrict(),
            unitSingle = args.firstOrNull("unitSingle"),
            unitMultiple = args.firstOrNull("unitMultiple"),
        )

        log.info("request:\n{}", req)

        val response = this.productApi.update(productId, req)

        log.info("patched product:\n{}", response)
    }

    fun getProduct(
        freeArgs: List<String>,
    ) = if (freeArgs.isEmpty()) {

        val response = this.productApi.readAll()
        this.log.info("products:\n{}", response)

    }
    else {

        val productId = this.entityIdService.findUUID("product", freeArgs.first())

        val response = this.productApi.read(productId)

        log.info("product:\n{}", response)

    }

    fun getLastProduct(doLog: Boolean): ProductApi.ReadDto {

        val productId = this.entityIdService.findUUIDOrLast("product", null)
        val response = this.productApi.read(productId)

        if (doLog)
            log.info("product:\n{}", response)

        return response
    }

}
