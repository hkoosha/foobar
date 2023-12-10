package io.koosha.foobar.customer.api.model.dto

import jakarta.validation.Valid

data class CustomerUpdateRequestDto(
    @field:Valid
    val name: CustomerUpdateRequestNameDto? = null,
)
