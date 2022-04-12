package io.koosha.foobar.shipping.api.service

import io.koosha.foobar.shipping.api.model.ShippingState
import java.util.*


data class ShippingCreateRequest(
    var sellerId: UUID,
    var orderRequestId: UUID,
)


data class ShippingUpdateRequest(

    var state: ShippingState? = null,
)

