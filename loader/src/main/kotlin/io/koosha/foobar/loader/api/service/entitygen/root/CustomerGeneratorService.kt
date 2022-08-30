package io.koosha.foobar.loader.api.service.entitygen.root

import io.koosha.foobar.connect.customer.generated.api.AddressApi
import io.koosha.foobar.connect.customer.generated.api.CustomerAddressCreateRequest
import io.koosha.foobar.connect.customer.generated.api.CustomerApi
import io.koosha.foobar.connect.customer.generated.api.CustomerCreateRequest
import io.koosha.foobar.connect.customer.generated.api.CustomerCreateRequestName
import io.koosha.foobar.loader.api.service.storage.IdStorageService
import io.koosha.foobar.loader.api.service.Rand
import mu.KLogger
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.util.*


@Service
class CustomerGeneratorService(
    private val rand: Rand,

    private val addressApi: AddressApi,
    private val customerApi: CustomerApi,

    private val ids: IdStorageService,
) : RootEntityGeneratorService {

    override val log: KLogger = KotlinLogging.logger {}


    override fun generate(): Boolean {

        val req = CustomerCreateRequest()
        req.name = CustomerCreateRequestName()
        req.name.firstName = this.rand.string(10, "fistName-")
        req.name.lastName = this.rand.string(10, "lastName-")
        req.name.title = this.rand.selectEnum(CustomerCreateRequestName.TitleEnum::class.java)

        val response = this.customerApi.postCustomerWithHttpInfo(req)
        if (response.statusCode < 200 || response.statusCode > 299)
            return false

        val entity = response.data
        this.ids.addCustomer(entity.customerId)

        var ok = true
        for (i in 0 until this.rand.int(min = 1, max = 4))
            if (!this.tryGenerateAddress(entity.customerId))
                ok = false

        return ok
    }


    private fun tryGenerateAddress(customerId: UUID): Boolean = try {
        this.generateAddress(customerId)
        true
    }
    catch (e: Exception) {
        log.error("generateAddress customerId=$customerId error: ${e.javaClass.name} -> ${e.message}")
        false
    }

    private fun generateAddress(customerId: UUID) {

        val req = CustomerAddressCreateRequest()
        req.name = this.rand.string(10, "ad-name-")
        req.city = this.rand.string(10, "city-")
        req.country = this.rand.string(10, "country-")
        req.zipcode = this.rand.string(5)
        req.addressLine1 = this.rand.string(10, "addressLine1-")

        this.addressApi.postAddress(customerId, req)
    }

}
