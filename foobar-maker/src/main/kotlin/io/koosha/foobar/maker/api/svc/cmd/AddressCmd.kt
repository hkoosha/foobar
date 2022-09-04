package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.connect.customer.generated.api.Address
import io.koosha.foobar.connect.customer.generated.api.AddressApi
import io.koosha.foobar.connect.customer.generated.api.ApiResponse
import io.koosha.foobar.connect.customer.generated.api.CustomerAddressCreateRequest
import io.koosha.foobar.connect.customer.generated.api.CustomerApi
import io.koosha.foobar.maker.api.CliException
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.assertStatusCode
import io.koosha.foobar.maker.api.firstOrRandom
import io.koosha.foobar.maker.api.firstOrRandomUnPrefixed
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.svc.EntityIdService
import mu.KotlinLogging
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.util.*


@Component
class AddressCmd(
    private val addressApi: AddressApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = KotlinLogging.logger {}

    override val commandName: String = AddressApi.ENTITY_TYPE

    override fun handle(
        args: ApplicationArguments,
        freeArgs: List<String>,
    ) = when {
        freeArgs.isEmpty() -> this.postAddress(
            args,
            freeArgs,
        )

        matches("post", freeArgs[0]) -> this.postAddress(
            args,
            freeArgs.subList(1, freeArgs.size)
        )

        matches("get", freeArgs[0]) -> this.getAddress(
            freeArgs.subList(1, freeArgs.size)
        )

        matches("last", freeArgs[0]) -> {
            this.getLastCustomerAddresses(true)
            Unit
        }

        else -> log.error { "unknown address command: ${freeArgs[0]}" }
    }

    fun postAddress(
        args: ApplicationArguments,
        freeArgs: List<String>,
        doLog: Boolean = true,
    ) {

        val customerId: UUID = this.entityIdService.findUUIDOrLast(CustomerApi.ENTITY_TYPE, freeArgs.firstOrNull())

        val req = CustomerAddressCreateRequest()
        req.name = args.firstOrRandom("name")
        req.city = args.firstOrRandom("city")
        req.country = args.firstOrRandom("country")
        req.zipcode = args.firstOrRandomUnPrefixed("zipcode")
        req.addressLine1 = args.firstOrRandom("addressLine1")

        if (doLog)
            log.info { "request:\n$req" }
        val response = this.addressApi.postAddressWithHttpInfo(customerId, req)
        assertStatusCode(response.statusCode)
        val entity = response.data

        val id = this.entityIdService
            .findMaxInternalIdByEntityType(AddressApi.ENTITY_TYPE)
            .map { it + 1 }
            .orElse(0L)
        this.entityIdService.save(
            EntityId(
                entityId = "$customerId/${entity.addressId}",
                internalId = id,
                entityType = AddressApi.ENTITY_TYPE,
            )
        )

        if (doLog)
            log.info { "posted address:\n${response.headers}\n\n$entity" }
    }

    fun getAddress(freeArgs: List<String>) {

        val customerId: UUID = this.entityIdService.findUUIDOrLast(CustomerApi.ENTITY_TYPE, freeArgs.firstOrNull())
        val addressId: Long? =
            if (freeArgs.size > 1) {
                try {
                    freeArgs[1].toLong()
                }
                catch (ex: NumberFormatException) {
                    throw CliException("invalid addressId, expecting valid long, got=${freeArgs[1]}")
                }
            }
            else {
                null
            }

        if (addressId == null) {

            val response = this.addressApi.getAddressesWithHttpInfo(customerId)
            assertStatusCode(response.statusCode)
            val all = response.data
            this.log.info { "customer addresses:\n$all" }

        }
        else {

            val response = this.addressApi.getAddressWithHttpInfo(customerId, addressId)
            assertStatusCode(response.statusCode)
            val entity = response.data
            log.info { "customer address:\n$entity" }

        }
    }

    fun getLastCustomerAddresses(doLog: Boolean): List<Address> {

        val customerId: UUID = this.entityIdService.findUUIDOrLast(CustomerApi.ENTITY_TYPE, null)
        val response: ApiResponse<MutableList<Address>> = this.addressApi.getAddressesWithHttpInfo(customerId)
        assertStatusCode(response.statusCode)
        val entities: List<Address> = response.data
        if (doLog)
            log.info { "addresses:\n${response.headers}\n$entities" }
        return entities
    }

}
