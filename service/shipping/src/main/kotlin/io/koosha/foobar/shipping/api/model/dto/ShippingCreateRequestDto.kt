package io.koosha.foobar.shipping.api.model.dto

import java.util.UUID

data class ShippingCreateRequestDto(
    var sellerId: UUID,
    var orderRequestId: UUID,
)
