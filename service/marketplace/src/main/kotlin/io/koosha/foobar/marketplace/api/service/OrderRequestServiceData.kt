package io.koosha.foobar.marketplace.api.service

import io.koosha.foobar.marketplace.api.model.OrderRequestState
import java.util.*
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull


data class OrderRequestCreateRequest(

    @field:NotNull
    val customerId: UUID?,
)

data class OrderRequestUpdateRequest(

    val sellerId: UUID? = null,

    var state: OrderRequestState? = null,

    @field:Min(0)
    var subTotal: Long? = null,
)

data class LineItemRequest(

    @field:NotNull
    val productId: UUID?,

    @field:NotNull
    @field:Min(0)
    val units: Long?,
)

data class LineItemUpdateRequest(

    @field:Min(0)
    val units: Long?,
)
