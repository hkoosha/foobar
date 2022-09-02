package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.connect.customer.generated.api.Customer
import io.koosha.foobar.connect.customer.generated.api.CustomerApi
import io.koosha.foobar.connect.customer.generated.api.CustomerCreateRequest
import io.koosha.foobar.connect.customer.generated.api.CustomerCreateRequestName
import io.koosha.foobar.connect.customer.generated.api.CustomerUpdateRequest
import io.koosha.foobar.connect.customer.generated.api.CustomerUpdateRequestName
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.assertStatusCode
import io.koosha.foobar.maker.api.first
import io.koosha.foobar.maker.api.firstOrNull
import io.koosha.foobar.maker.api.firstOrRandom
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.stringAll
import io.koosha.foobar.maker.api.svc.EntityIdService
import io.koosha.foobar.maker.api.svc.Rand
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component


@Component
class CustomerCmd(
    private val rand: Rand,
    private val customerApi: CustomerApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = KotlinLogging.logger {}

    override val commandName: String = CustomerApi.ENTITY_TYPE

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

        else -> log.error { "unknown customer command: ${freeArgs[0]}" }
    }

    fun postCustomer(
        args: ApplicationArguments,
        doLog: Boolean = true,
    ) {

        val req = CustomerCreateRequest()
        req.name = CustomerCreateRequestName()
        req.name.firstName = args.firstOrRandom("firstName")
        req.name.lastName = args.firstOrRandom("lastName")
        req.name.title =
            if (args.firstOrNull("title") == null)
                this.rand.selectEnum(CustomerCreateRequestName.TitleEnum::class.java)
            else
                CustomerCreateRequestName.TitleEnum.valueOf(args.first("title"))

        if (doLog)
            log.info { "request:\n$req" }
        val response = this.customerApi.postCustomerWithHttpInfo(req)
        assertStatusCode(response.statusCode)
        val entity = response.data

        val id = this.entityIdService
            .findMaxInternalIdByEntityType(CustomerApi.ENTITY_TYPE)
            .map { it + 1 }
            .orElse(0L)
        this.entityIdService.save(
            EntityId(
                entityId = entity.customerId.toString(),
                internalId = id,
                entityType = CustomerApi.ENTITY_TYPE,
            )
        )

        if (doLog)
            log.info { "posted customer:\n${response.headers}\nId=$id\nentity:\n$entity" }
    }

    fun patchCustomer(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) {

        val customerId = this.entityIdService.findUUIDOrLast(CustomerApi.ENTITY_TYPE, freeArgs.firstOrNull())

        val req = CustomerUpdateRequest()
        req.name = CustomerUpdateRequestName()
        req.name!!.firstName = args.firstOrNull("firstName")
        req.name!!.lastName = args.firstOrNull("lastName")
        req.name!!.title =
            if (args.firstOrNull("title") == null)
                null
            else
                CustomerUpdateRequestName.TitleEnum.valueOf(args.first("title"))

        log.info { "request:\n$req" }
        val response = this.customerApi.patchCustomerWithHttpInfo(customerId, req)
        assertStatusCode(response.statusCode)
        val entity = response.data
        log.info { "patched customer:\n${response.headers}\n$entity" }
    }

    fun getCustomer(
        freeArgs: List<String>,
    ) =
        if (freeArgs.isEmpty()) {

            val response = this.customerApi.customersWithHttpInfo
            assertStatusCode(response.statusCode)
            val all = response.data
            val s = stringAll(this.entityIdService, CustomerApi.ENTITY_TYPE, all) { it.customerId.toString() }
            log.info { s }

        }
        else {

            val customerId = this.entityIdService.findUUID(CustomerApi.ENTITY_TYPE, freeArgs.first())
            val response = this.customerApi.getCustomerWithHttpInfo(customerId)
            assertStatusCode(response.statusCode)
            val entity: Customer? = response.data
            log.info { "customer:\n${response.headers}\n$entity" }

        }

    fun getLastCustomer(doLog: Boolean): Customer? {

        val customerId = this.entityIdService.findUUIDOrLast(CustomerApi.ENTITY_TYPE, null)
        val response = this.customerApi.getCustomerWithHttpInfo(customerId)
        val entity: Customer? = response.data
        if (doLog)
            log.info { "customer:\n${response.headers}\n$entity" }
        return entity
    }

}
