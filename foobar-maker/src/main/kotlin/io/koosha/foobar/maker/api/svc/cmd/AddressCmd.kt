package io.koosha.foobar.maker.api.svc.cmd

import io.koosha.foobar.maker.api.CliException
import io.koosha.foobar.maker.api.Command
import io.koosha.foobar.maker.api.connect.CustomerAddressApi
import io.koosha.foobar.maker.api.firstOrRandom
import io.koosha.foobar.maker.api.firstOrRandomUnPrefixed
import io.koosha.foobar.maker.api.matches
import io.koosha.foobar.maker.api.model.EntityId
import io.koosha.foobar.maker.api.svc.EntityIdService
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class AddressCmd(
    private val addressApi: CustomerAddressApi,
    private val entityIdService: EntityIdService,
) : Command {

    private val log = LoggerFactory.getLogger(this::class.java)

    override val commandName: String = "address"

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

        else -> log.error("unknown address command: {}", freeArgs[0])
    }

    fun postAddress(
        args: ApplicationArguments,
        freeArgs: List<String>,
        doLog: Boolean = true,
    ) {

        val customerId: UUID = this.entityIdService.findUUIDOrLast("customer", freeArgs.firstOrNull())

        val req = CustomerAddressApi.CreateDto(
            name = args.firstOrRandom("name"),
            city = args.firstOrRandom("city"),
            country = args.firstOrRandom("country"),
            zipcode = args.firstOrRandomUnPrefixed("zipcode"),
            addressLine1 = args.firstOrRandom("addressLine1"),
        )

        if (doLog)
            log.info("request:\n{}", req)
        val response = this.addressApi.create(customerId, req)

        val id = this.entityIdService
            .findMaxInternalIdByEntityType("address")
            .map { it + 1 }
            .orElse(0L)
        this.entityIdService.save(
            EntityId(
                entityId = "$customerId/${response.addressId}",
                internalId = id,
                entityType = "address",
            )
        )

        if (doLog)
            log.info("posted address: {}", response)
    }

    fun getAddress(
        freeArgs: List<String>,
    ) {

        val customerId: UUID = this.entityIdService.findUUIDOrLast("customer", freeArgs.firstOrNull())
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

            val response = this.addressApi.readAll(customerId)
            this.log.info("customer addresses:\n{}", response)

        }
        else {

            val response = this.addressApi.read(customerId, addressId)
            log.info("customer address:\n{}", response)

        }
    }

    fun getLastCustomerAddresses(
        doLog: Boolean,
    ): Collection<CustomerAddressApi.ReadDto> {

        val customerId: UUID = this.entityIdService.findUUIDOrLast("customer", null)
        val response = this.addressApi.readAll(customerId)
        if (doLog)
            log.info("addresses:\n{}", response)
        return response
    }

}
