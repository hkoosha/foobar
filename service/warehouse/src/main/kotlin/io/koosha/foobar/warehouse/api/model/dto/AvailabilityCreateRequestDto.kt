package io.koosha.foobar.warehouse.api.model.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.util.UUID

data class AvailabilityCreateRequestDto(
    @field:NotNull
    val sellerId: UUID?,

    @field:Min(0)
    @field:NotNull
    val unitsAvailable: Long?,

    @field:Min(0)
    @field:NotNull
    val pricePerUnit: Long?,
)
