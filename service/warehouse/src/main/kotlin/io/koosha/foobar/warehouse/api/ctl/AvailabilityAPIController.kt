package io.koosha.foobar.warehouse.api.ctl

import io.koosha.foobar.warehouse.api.model.dto.AvailabilityCreateRequestDto
import io.koosha.foobar.warehouse.api.model.dto.AvailabilityUpdateRequestDto
import io.koosha.foobar.warehouse.api.model.entity.AvailabilityDO
import io.koosha.foobar.warehouse.api.service.ProductService
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


// We fake sellerId for availabilityId, although prone to timing attacks on access checks.
@RestController
@RequestMapping("/foobar/warehouse/v1/product/{productId}/availability")
@Tags(
    Tag(name = "availability")
)
class AvailabilityAPIController(
    private val service: ProductService,
) {

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    fun create(
        @PathVariable
        productId: UUID,

        @RequestBody
        request: AvailabilityCreateRequestDto,

        response: HttpServletResponse,
    ): Availability {

        val entity: AvailabilityDO = this.service.addAvailability(productId, request)

        val location = MvcUriComponentsBuilder
            .fromMethodName(
                AvailabilityAPIController::class.java,
                "read",
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

    @Transactional(readOnly = true)
    @GetMapping
    @ResponseBody
    fun readAll(
        @PathVariable
        productId: UUID,
    ): List<Availability> =
        this.service
            .getAvailabilitiesOf(productId)
            .map(::Availability)

    @Transactional(readOnly = true)
    @GetMapping("/{sellerId}")
    @ResponseBody
    fun read(
        @PathVariable
        productId: UUID,

        @PathVariable
        sellerId: UUID,
    ): Availability = Availability(this.service.getAvailability(productId, sellerId))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @PatchMapping("{sellerId}")
    @ResponseBody
    fun update(
        @PathVariable
        productId: UUID,

        @PathVariable
        sellerId: UUID,

        @RequestBody
        request: AvailabilityUpdateRequestDto,
    ): Availability = Availability(this.service.updateAvailability(productId, sellerId, request))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @DeleteMapping("/{sellerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
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
