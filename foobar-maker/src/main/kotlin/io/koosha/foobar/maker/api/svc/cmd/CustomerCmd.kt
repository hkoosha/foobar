package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.connect.CustomerApi
import io.koosha.foobar.maker.api.first
import io.koosha.foobar.maker.api.firstOrNull
import io.koosha.foobar.maker.api.firstOrRandom
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.stringAll
import io.koosha.foobar.maker.api.svc.EntityIdService
import io.koosha.foobar.maker.api.svc.Rand
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component

@Component
class CustomerCmd(
    private val rand: Rand,
    private val customerApi: CustomerApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = LoggerFactory.getLogger(this::class.java)

    override val commandName: String = "customer"

    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) = when {
        freeArgs.isEmpty() -> this.postCustomer(
            args,
        )

        matches("post", freeArgs[0]) -> this.postCustomer(
            args,
        )

        matches("patch", freeArgs[0]) -> this.patchCustomer(
            args,
            freeArgs.subList(1, freeArgs.size)
        )

        matches("get", freeArgs[0]) -> this.getCustomer(
            freeArgs.subList(1, freeArgs.size)
        )

        matches("last", freeArgs[0]) -> {
            this.getLastCustomer(true)
            Unit
        }

        else -> log.error("unknown customer command: {}", freeArgs[0])
    }

    fun postCustomer(
        args: ApplicationArguments,
        doLog: Boolean = true,
    ) {

        val req = CustomerApi.CreateDto(
            name = CustomerApi.NameDto(
                firstName = args.firstOrRandom("firstName"),
                lastName = args.firstOrRandom("lastName"),
                title = if (args.firstOrNull("title") == null)
                    this.rand.selectEnum(CustomerApi.Title::class.java)
                else
                    CustomerApi.Title.valueOf(args.first("title"))
            )
        )

        if (doLog)
            log.info("request:\n{}", req)

        val response = this.customerApi.create(req)

        val id = this.entityIdService
            .findMaxInternalIdByEntityType("customer")
            .map { it + 1 }
            .orElse(0L)
        this.entityIdService.save(
            EntityId(
                entityId = response.customerId.toString(),
                internalId = id,
                entityType = "customer",
            )
        )

        if (doLog)
            log.info("posted customer:\nId={}\nentity:\n{}", id, response)
    }

    fun patchCustomer(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val customerId = this.entityIdService.findUUIDOrLast("customer", freeArgs.firstOrNull())

        val req = CustomerApi.UpdateDto(
            name = CustomerApi.NameUpdateDto(
                firstName = args.firstOrNull("firstName"),
                lastName = args.firstOrNull("lastName"),
                title = if (args.firstOrNull("title") == null)
                    null
                else
                    CustomerApi.Title.valueOf(args.first("title"))
            )
        )

        log.info("request:\n{}", req)
        val response = this.customerApi.update(customerId, req)
        log.info("patched customer:\n{}", response)
    }

    fun getCustomer(
        freeArgs: List<String>,
    ) =
        if (freeArgs.isEmpty()) {

            val response = this.customerApi.readAll()
            val s = stringAll(this.entityIdService, "customer", response) {
                it.customerId.toString()
            }
            log.info(s)

        }
        else {

            val customerId = this.entityIdService.findUUID("customer", freeArgs.first())
            val response = this.customerApi.read(customerId)
            log.info("customer:\n{}", response)

        }

    fun getLastCustomer(doLog: Boolean): CustomerApi.ReadDto {

        val customerId = this.entityIdService.findUUIDOrLast("customer", null)
        val response = this.customerApi.read(customerId)
        if (doLog)
            log.info("customer:\n{}", response)
        return response
    }

}
