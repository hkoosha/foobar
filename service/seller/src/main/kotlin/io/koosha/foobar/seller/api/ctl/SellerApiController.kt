package io.koosha.foobar.seller.api.ctl

import io.koosha.foobar.seller.API_PATH_PREFIX
import io.koosha.foobar.seller.api.model.SellerDO
import io.koosha.foobar.seller.api.model.SellerState
import io.koosha.foobar.seller.api.service.SellerCreateRequest
import io.koosha.foobar.seller.api.service.SellerService
import io.koosha.foobar.seller.api.service.SellerUpdateRequest
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import jakarta.servlet.http.HttpServletResponse
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


@RestController
@RequestMapping(SellerApiController.URI)
@Tags(
    Tag(name = SellerDO.ENTITY_TYPE)
)
class SellerApiController(
    private val service: SellerService,
) {

    companion object {

        const val URI = "/$API_PATH_PREFIX/sellers"
        const val URI__PART__SELLER_ID = "sellerId"

    }

    @GetMapping
    @ResponseBody
    fun getSellers(): List<Seller> = service.findAll().map(::Seller)

    @GetMapping("/{$URI__PART__SELLER_ID}")
    @ResponseBody
    fun getSeller(
        @PathVariable
        sellerId: UUID,
    ): Seller = Seller(service.findByIdOrFail(sellerId))

    @PatchMapping("/{$URI__PART__SELLER_ID}")
    @ResponseBody
    fun patchSeller(
        @PathVariable
        sellerId: UUID,
        @RequestBody
        request: SellerUpdateRequest,
    ): Seller = Seller(service.update(sellerId, request))

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun postSeller(
        @RequestBody
        request: SellerCreateRequest,
        response: HttpServletResponse,
    ): Seller {

        val entity: SellerDO = this.service.create(request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                SellerApiController::class.java,
                "getSeller",
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

    @DeleteMapping("/{$URI__PART__SELLER_ID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteSeller(
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
