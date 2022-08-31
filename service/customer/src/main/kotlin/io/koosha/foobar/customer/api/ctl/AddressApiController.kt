package io.koosha.foobar.customer.api.ctl

import io.koosha.foobar.customer.api.model.AddressDO
import io.koosha.foobar.customer.api.service.CustomerAddressCreateRequest
import io.koosha.foobar.customer.api.service.CustomerService
import io.micrometer.core.annotation.Timed
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder
import java.util.*
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping(AddressApiController.URI)
@Tags(
    Tag(name = AddressDO.ENTITY_TYPE)
)
class AddressApiController(
    private val service: CustomerService,
) {

    companion object {

        const val URI__PART__ADDRESS_ID = "addressId"
        const val URI = "${CustomerApiController.URI}/{${CustomerApiController.URI__PART__CUSTOMER_ID}}/addresses"

    }

    @Timed
    @GetMapping
    @ResponseBody
    fun getAddresses(
        @PathVariable
        customerId: UUID,
    ): List<Address> = service.getAddressesOfCustomer(customerId).map(::Address)

    @Timed
    @GetMapping("/{$URI__PART__ADDRESS_ID}")
    @ResponseBody
    fun getAddress(
        @PathVariable
        customerId: UUID,
        @PathVariable
        addressId: Long,
    ): Address = Address(service.getAddress(customerId, addressId))

    @Timed
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun postAddress(
        @PathVariable
        customerId: UUID,
        @RequestBody
        request: CustomerAddressCreateRequest,
        response: HttpServletResponse,
    ): Address {

        val entity = this.service.addAddress(customerId, request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                AddressApiController::class.java,
                "getAddress",
                customerId,
                entity.addressPk.addressId,
            )
            .buildAndExpand(
                customerId,
                entity.addressPk.addressId,
            )
            .toUri()
            .toASCIIString()
        response.setHeader(HttpHeaders.LOCATION, location)

        return Address(entity)
    }

    @Timed
    @DeleteMapping("/{$URI__PART__ADDRESS_ID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAddress(
        @PathVariable
        customerId: UUID,
        @PathVariable
        addressId: Long,
    ) = this.service.deleteAddress(customerId, addressId)


    data class Address(

        val addressId: Long,
        val name: String,
        val zipcode: String,
        val addressLine1: String,
        val country: String,
        val city: String,

        ) {

        constructor(entity: AddressDO) : this(
            addressId = entity.addressPk.addressId!!,
            name = entity.name!!,
            zipcode = entity.zipcode!!,
            addressLine1 = entity.addressLine1!!,
            country = entity.country!!,
            city = entity.city!!,
        )
    }

}
