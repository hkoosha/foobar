package io.koosha.foobar.shipping.api.model.dto

import io.koosha.foobar.shipping.api.model.ShippingState

data class ShippingUpdateRequestDto(
    var state: ShippingState? = null,
)
