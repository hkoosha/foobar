package io.koosha.foobar.shipping.api.ctl

import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.koosha.foobar.shipping.api.model.dto.ShippingUpdateRequestDto
import io.koosha.foobar.shipping.api.model.entity.ShippingDO
import io.koosha.foobar.shipping.api.model.ShippingState
import io.koosha.foobar.shipping.api.service.ShippingService
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID


@RestController
@RequestMapping("/foobar/shipping/v1/shipping")
class ShippingApiController(
    private val service: ShippingService,
) {

    @Transactional(readOnly = true)
    @GetMapping
    @ResponseBody
    fun read(): List<Shipping> =
        this.service
            .findAll()
            .map(::Shipping)

    @Transactional(readOnly = true)
    @GetMapping("/{shippingId}")
    @ResponseBody
    fun read(
        @PathVariable
        shippingId: UUID,
    ): Shipping = Shipping(this.service.findByIdOrFail(shippingId))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @PatchMapping("/{shippingId}")
    @ResponseBody
    @Bulkhead(name = "patch-shipping")
    fun update(
        @PathVariable
        shippingId: UUID,
        @RequestBody
        request: ShippingUpdateRequestDto,
    ): Shipping = Shipping(this.service.update(shippingId, request))

    @Transactional(
        rollbackFor = [Exception::class],
    )
    @DeleteMapping("/{shippingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @PathVariable
        shippingId: UUID,
    ) = this.service.delete(shippingId)


    data class Address(
        var addressLine1: String,
        var city: String,
        var zipCode: String,
        var country: String,
    )

    data class Shipping(
        var shippingId: UUID,
        var orderRequestId: UUID,
        var pickupAddress: Address,
        var deliveryAddress: Address,
        var state: ShippingState,
    ) {

        constructor(entity: ShippingDO) : this(
            shippingId = entity.shippingId!!,
            orderRequestId = entity.orderRequestId!!,
            pickupAddress = Address(
                zipCode = entity.pickupAddress.zipcode!!,
                addressLine1 = entity.pickupAddress.addressLine1!!,
                country = entity.pickupAddress.country!!,
                city = entity.pickupAddress.city!!,
            ),
            deliveryAddress = Address(
                zipCode = entity.deliveryAddress.zipcode!!,
                addressLine1 = entity.deliveryAddress.addressLine1!!,
                country = entity.deliveryAddress.country!!,
                city = entity.deliveryAddress.city!!,
            ),
            state = entity.state!!,
        )
    }

}
