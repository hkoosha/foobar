package io.koosha.foobar.warehouse.api.ctl

import io.koosha.foobar.warehouse.API_PATH_PREFIX
import io.koosha.foobar.warehouse.api.model.AvailabilityDO
import io.koosha.foobar.warehouse.api.service.AvailabilityCreateRequest
import io.koosha.foobar.warehouse.api.service.AvailabilityUpdateRequest
import io.koosha.foobar.warehouse.api.service.ProductService
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


// We fake sellerId for availabilityId, although prone to timing attacks on access checks.
@RestController
@RequestMapping(AvailabilityAPIController.URI)
@Tags(
    Tag(name = AvailabilityDO.ENTITY_TYPE)
)
class AvailabilityAPIController(
    private val service: ProductService,
) {

    companion object {

        const val URI = "/$API_PATH_PREFIX/products/{${ProductAPIController.URI__PART__PRODUCT_ID}}/availabilities"
        const val URI__PART__SELLER_ID = "sellerId"

    }

    @GetMapping
    @ResponseBody
    fun getAvailabilities(
        @PathVariable
        productId: UUID,
    ): List<Availability> = service.getAvailabilitiesOf(productId).map(::Availability)

    @GetMapping("/{$URI__PART__SELLER_ID}")
    @ResponseBody
    fun getAvailability(
        @PathVariable
        productId: UUID,
        @PathVariable
        sellerId: UUID,
    ): Availability = Availability(service.getAvailability(productId, sellerId))

    @PatchMapping("{$URI__PART__SELLER_ID}")
    @ResponseBody
    fun patchAvailability(
        @PathVariable
        productId: UUID,
        @PathVariable
        sellerId: UUID,
        @RequestBody
        request: AvailabilityUpdateRequest,
    ): Availability = Availability(service.updateAvailability(productId, sellerId, request))

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun postAvailability(
        @PathVariable
        productId: UUID,
        @RequestBody
        request: AvailabilityCreateRequest,
        response: HttpServletResponse,
    ): Availability {

        val entity: AvailabilityDO = this.service.addAvailability(productId, request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                AvailabilityAPIController::class.java,
                "getAvailability",
                productId,
                entity.availabilityPk.sellerId,
            )
            .buildAndExpand(
                productId,
                entity.availabilityPk.sellerId,
            )
            .toUri()
            .toASCIIString()
        response.setHeader(HttpHeaders.LOCATION, location)

        return Availability(entity)
    }

    @DeleteMapping("/{$URI__PART__SELLER_ID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAvailability(
        @PathVariable
        productId: UUID,
        @PathVariable
        sellerId: UUID,
    ) = this.service.deleteAvailability(productId, sellerId)


    data class Availability(
        val sellerId: UUID,
        val unitsAvailable: Long,
        val frozenUnits: Long,
        val pricePerUnit: Long,
    ) {

        constructor(entity: AvailabilityDO) : this(
            sellerId = entity.availabilityPk.sellerId!!,
            unitsAvailable = entity.unitsAvailable!!,
            frozenUnits = entity.frozenUnits!!,
            pricePerUnit = entity.pricePerUnit!!,
        )
    }
}
