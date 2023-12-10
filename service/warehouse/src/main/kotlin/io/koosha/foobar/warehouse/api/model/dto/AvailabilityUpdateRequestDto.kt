package io.koosha.foobar.warehouse.api.model.dto

import jakarta.validation.constraints.Min

data class AvailabilityUpdateRequestDto(
    @field:Min(0)
    val unitsAvailable: Long?,

    @field:Min(0)
    val unitsToFreeze: Long?,

    @field:Min(0)
    val pricePerUnit: Long?,
)
