package io.koosha.foobar.loader.api.service.entitygen.root

import io.koosha.foobar.loader.api.connect.CustomerAddressApi
import io.koosha.foobar.loader.api.connect.CustomerApi
import io.koosha.foobar.loader.api.service.Rand
import io.koosha.foobar.loader.api.service.storage.IdStorageService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID


@Service
class CustomerGeneratorService(
    private val rand: Rand,
    private val addressApi: CustomerAddressApi,
    private val customerApi: CustomerApi,
    private val ids: IdStorageService,
) : RootEntityGeneratorService {

    override val log: Logger = LoggerFactory.getLogger(this::class.java)

    override fun generate(): Boolean {

        val req = CustomerApi.CreateDto(
            name = CustomerApi.NameDto(
                firstName = this.rand.string(10, "firstName-"),
                lastName = this.rand.string(10, "lastName-"),
                title = this.rand.selectEnum(CustomerApi.Title::class.java),
            )
        )

        val response = try {
            this.customerApi.create(req)
        }
        catch (e: Exception) {
            log.error("error", e)
            return false
        }

        this.ids.addCustomer(response.customerId)

        var ok = true
        for (i in 0 until this.rand.int(min = 1, max = 4))
            if (!this.tryGenerateAddress(response.customerId))
                ok = false

        return ok
    }

    private fun tryGenerateAddress(customerId: UUID): Boolean = try {
        this.generateAddress(customerId)
        true
    }
    catch (e: Exception) {
        log.error("customerId={}", customerId, e)
        false
    }

    private fun generateAddress(customerId: UUID) {

        val req = CustomerAddressApi.CreateDto(
            name = this.rand.string(10, "adrName-"),
            city = this.rand.string(10, "city-"),
            country = this.rand.string(10, "country-"),
            zipcode = this.rand.string(5),
            addressLine1 = this.rand.string(10, "adrLine1-"),
        )

        this.addressApi.create(customerId, req)
    }

}
