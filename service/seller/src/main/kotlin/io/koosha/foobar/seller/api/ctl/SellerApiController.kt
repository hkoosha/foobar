package io.koosha.foobar.seller.api.ctl

import io.koosha.foobar.seller.api.model.SellerState
import io.koosha.foobar.seller.api.model.dto.SellerCreateRequestDto
import io.koosha.foobar.seller.api.model.dto.SellerUpdateRequestDto
import io.koosha.foobar.seller.api.model.entity.SellerDO
import io.koosha.foobar.seller.api.service.SellerService
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
@RequestMapping("/foobar/seller/v1/seller")
@Tags(
    Tag(name = "seller")
)
class SellerApiController(
    private val service: SellerService,
) {

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @RequestBody
        request: SellerCreateRequestDto,

        response: HttpServletResponse,
    ): Seller {

        val entity: SellerDO = this.service.create(request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                SellerApiController::class.java,
                "read",
                entity.sellerId,
            )
            .buildAndExpand(
                entity.sellerId,
            )
            .toUri()
            .toASCIIString()
        response.setHeader(HttpHeaders.LOCATION, location)

        return Seller(entity)
    }

    @Transactional(readOnly = true)
    @GetMapping
    @ResponseBody
    fun readAll(): List<Seller> =
        this.service.findAll().map(::Seller)

    @Transactional(readOnly = true)
    @GetMapping("/{sellerId}")
    @ResponseBody
    fun read(
        @PathVariable
        sellerId: UUID,
    ): Seller = Seller(this.service.findByIdOrFail(sellerId))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @PatchMapping("/{sellerId}")
    @ResponseBody
    fun update(
        @PathVariable
        sellerId: UUID,

        @RequestBody
        request: SellerUpdateRequestDto,
    ): Seller = Seller(this.service.update(sellerId, request))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @DeleteMapping("/{sellerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable
        sellerId: UUID,
    ) = this.service.delete(sellerId)


    data class Address(

        val zipcode: String,
        val addressLine1: String,
        val country: String,
        val city: String,
    )

    data class Seller(

        val sellerId: UUID,
        val name: String,
        val address: Address,
        val isActive: Boolean,
    ) {

        constructor(entity: SellerDO) : this(
            sellerId = entity.sellerId!!,
            name = entity.name!!,
            address = Address(
                zipcode = entity.address.zipcode!!,
                addressLine1 = entity.address.addressLine1!!,
                country = entity.address.country!!,
                city = entity.address.city!!,
            ),
            isActive = entity.state == SellerState.ACTIVE,
        )
    }
}
