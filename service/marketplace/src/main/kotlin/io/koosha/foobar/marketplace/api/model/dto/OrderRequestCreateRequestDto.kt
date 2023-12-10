package io.koosha.foobar.marketplace.api.model.dto

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class OrderRequestCreateRequestDto(
    @field:NotNull
    val customerId: UUID?,
)
