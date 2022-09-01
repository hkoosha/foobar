package io.koosha.foobar.shipping.api.ctl

import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.koosha.foobar.common.TAG
import io.koosha.foobar.common.TAG_VALUE
import io.koosha.foobar.shipping.API_PATH_PREFIX
import io.koosha.foobar.shipping.api.model.ShippingDO
import io.koosha.foobar.shipping.api.model.ShippingState
import io.koosha.foobar.shipping.api.service.ShippingService
import io.koosha.foobar.shipping.api.service.ShippingUpdateRequest
import io.micrometer.core.annotation.Timed
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@RequestMapping(ShippingApiController.URI)
class ShippingApiController(
    private val service: ShippingService,
) {

    companion object {

        const val URI = "/$API_PATH_PREFIX/shippings"
        const val URI__PART__SHIPPING_ID = "shippingId"

    }

    @Timed(extraTags = [TAG, TAG_VALUE])
    @GetMapping
    @ResponseBody
    fun getShippings(): List<Shipping> = this.service.findAll().map(::Shipping)

    @Timed(extraTags = [TAG, TAG_VALUE])
    @GetMapping("/{$URI__PART__SHIPPING_ID}")
    @ResponseBody
    fun getShipping(
        @PathVariable
        shippingId: UUID,
    ): Shipping = Shipping(this.service.findByIdOrFail(shippingId))

    @Timed(extraTags = [TAG, TAG_VALUE])
    @PatchMapping("/{$URI__PART__SHIPPING_ID}")
    @ResponseBody
    @Bulkhead(name = "patch-shipping")
    fun patchShipping(
        @PathVariable
        shippingId: UUID,
        @RequestBody
        request: ShippingUpdateRequest,
    ): Shipping = Shipping(this.service.update(shippingId, request))

    @Timed(extraTags = [TAG, TAG_VALUE])
    @DeleteMapping("/{$URI__PART__SHIPPING_ID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteShipping(
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
