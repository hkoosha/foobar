package io.koosha.foobar.marketplace.api.model.dto

import jakarta.validation.constraints.Min

data class LineItemUpdateRequestDto(
    @field:Min(0)
    val units: Long?,
)
