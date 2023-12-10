package io.koosha.foobar.marketplace.api.model.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class LineItemRequestDto(
    @field:NotNull
    val productId: UUID?,

    @field:NotNull
    @field:Min(0)
    val units: Long?,
)
