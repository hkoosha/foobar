package io.koosha.foobar.customer.api.ctl

import io.koosha.foobar.common.TAG
import io.koosha.foobar.common.TAG_VALUE
import io.koosha.foobar.customer.API_PATH_PREFIX
import io.koosha.foobar.customer.api.model.CustomerDO
import io.koosha.foobar.customer.api.model.CustomerState
import io.koosha.foobar.customer.api.model.Title
import io.koosha.foobar.customer.api.service.CustomerCreateRequest
import io.koosha.foobar.customer.api.service.CustomerService
import io.koosha.foobar.customer.api.service.CustomerUpdateRequest
import io.micrometer.core.annotation.Timed
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
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
@RequestMapping(CustomerApiController.URI)
@Tags(
    Tag(name = CustomerDO.ENTITY_TYPE)
)
class CustomerApiController(
    private val service: CustomerService,
) {

    companion object {

        const val URI__PART__CUSTOMER_ID = "customerId"
        const val URI = "/$API_PATH_PREFIX/customers"

    }

    @Timed(extraTags = [TAG, TAG_VALUE])
    @GetMapping
    @ResponseBody
    fun getCustomers(): List<Customer> = service.findAll().map(::Customer)

    @Timed(extraTags = [TAG, TAG_VALUE])
    @GetMapping("/{$URI__PART__CUSTOMER_ID}")
    @ResponseBody
    fun getCustomer(
        @PathVariable
        customerId: UUID,
    ): Customer = Customer(service.findByCustomerIdOrFail(customerId))

    @Timed(extraTags = [TAG, TAG_VALUE])
    @PatchMapping("/{$URI__PART__CUSTOMER_ID}")
    @ResponseBody
    fun patchCustomer(
        @PathVariable
        customerId: UUID,
        @RequestBody
        request: CustomerUpdateRequest,
    ): Customer = Customer(service.update(customerId, request))

    @Timed(extraTags = [TAG, TAG_VALUE])
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun postCustomer(
        @RequestBody
        request: CustomerCreateRequest,
        response: HttpServletResponse,
    ): Customer {

        val entity: CustomerDO = this.service.create(request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                CustomerApiController::class.java,
                "getCustomer",
                entity.customerId,
            )
            .buildAndExpand(
                entity.customerId,
            )
            .toUri()
            .toASCIIString()
        response.setHeader(HttpHeaders.LOCATION, location)

        return Customer(entity)
    }

    @Timed(extraTags = [TAG, TAG_VALUE])
    @DeleteMapping("/{$URI__PART__CUSTOMER_ID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(
        @PathVariable
        customerId: UUID,
    ) = this.service.delete(customerId)


    data class Name(

        val title: Title,
        val firstName: String,
        val lastName: String,
    )

    data class Customer(

        val customerId: UUID,
        val name: Name,
        val isActive: Boolean,

        ) {

        constructor(entity: CustomerDO) : this(
            customerId = entity.customerId!!,
            name = Name(
                title = entity.name.title!!,
                firstName = entity.name.firstName!!,
                lastName = entity.name.lastName!!,
            ),
            isActive = entity.state == CustomerState.ACTIVE,
        )
    }

}
