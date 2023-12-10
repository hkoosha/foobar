package io.koosha.foobar.customer.api.model.dto

import io.koosha.foobar.customer.api.model.Title
import io.koosha.foobar.customer.api.model.FIRST_NAME_MAX_LEN
import io.koosha.foobar.customer.api.model.LAST_NAME_MAX_LEN
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CustomerCreateRequestNameDto(
    @field:Valid
    @field:NotNull
    val title: Title?,

    @field:NotNull
    @field:Size(min = 1, max = FIRST_NAME_MAX_LEN)
    val firstName: String?,

    @field:NotNull
    @field:Size(min = 1, max = LAST_NAME_MAX_LEN)
    val lastName: String?,
)
