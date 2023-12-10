package io.koosha.foobar.marketplace.api.model.dto

import io.koosha.foobar.marketplace.api.model.OrderRequestState
import jakarta.validation.constraints.Min
import java.util.UUID

data class OrderRequestUpdateRequestDto(
    val sellerId: UUID? = null,

    var state: OrderRequestState? = null,

    @field:Min(0)
    var subTotal: Long? = null,
)
