package io.koosha.foobar.customer.api.ctl

import io.koosha.foobar.customer.api.model.dto.CustomerAddressCreateRequestDto
import io.koosha.foobar.customer.api.model.entity.AddressDO
import io.koosha.foobar.customer.api.service.CustomerService
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
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
import java.util.UUID

@RestController
@RequestMapping("/foobar/customer/v1/customer/{customerId}/address")
@Tags(
    Tag(name = "customer_address")
)
class AddressApiController(
    private val service: CustomerService,
) {

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @PathVariable
        customerId: UUID,

        @RequestBody
        request: CustomerAddressCreateRequestDto,

        response: HttpServletResponse,
    ): Address {

        val entity = this.service.addAddress(customerId, request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                AddressApiController::class.java,
                "read",
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

    @Transactional(readOnly = true)
    @GetMapping
    @ResponseBody
    fun read(
        @PathVariable
        customerId: UUID,
    ): List<Address> =
        this.service
            .getCustomerAddresses(customerId)
            .map(::Address)

    @Transactional(readOnly = true)
    @GetMapping("/{addressId}")
    @ResponseBody
    fun read(
        @PathVariable
        customerId: UUID,

        @PathVariable
        addressId: Long,
    ): Address = Address(this.service.getAddress(customerId, addressId))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @DeleteMapping("/{addressId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
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
