package io.koosha.foobar.customer.api.ctl

import io.koosha.foobar.customer.api.model.CustomerState
import io.koosha.foobar.customer.api.model.Title
import io.koosha.foobar.customer.api.model.dto.CustomerCreateRequestDto
import io.koosha.foobar.customer.api.model.dto.CustomerUpdateRequestDto
import io.koosha.foobar.customer.api.model.entity.CustomerDO
import io.koosha.foobar.customer.api.service.CustomerService
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
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
import java.util.UUID

@RestController
@RequestMapping("/foobar/customer/v1/customer")
@Tags(
    Tag(name = "customer")
)
class CustomerApiController(
    private val service: CustomerService,
) {

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody
        request: CustomerCreateRequestDto,

        response: HttpServletResponse,
    ): Customer {

        val entity: CustomerDO = this.service.create(request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                CustomerApiController::class.java,
                "read",
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

    @Transactional(readOnly = true)
    @GetMapping
    @ResponseBody
    fun read(): List<Customer> =
        this.service
            .findAll()
            .map(::Customer)

    @Transactional(readOnly = true)
    @GetMapping("/{customerId}")
    @ResponseBody
    fun read(
        @PathVariable
        customerId: UUID,
    ): Customer = Customer(this.service.findByCustomerIdOrFail(customerId))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @PatchMapping("/{customerId}")
    @ResponseBody
    fun update(
        @PathVariable
        customerId: UUID,

        @RequestBody
        request: CustomerUpdateRequestDto,
    ): Customer = Customer(this.service.update(customerId, request))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @DeleteMapping("/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
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
