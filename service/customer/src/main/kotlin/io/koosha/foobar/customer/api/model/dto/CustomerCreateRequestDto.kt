package io.koosha.foobar.customer.api.model.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class CustomerCreateRequestDto(
    @field:Valid
    @field:NotNull
    val name: CustomerCreateRequestNameDto?,
)
