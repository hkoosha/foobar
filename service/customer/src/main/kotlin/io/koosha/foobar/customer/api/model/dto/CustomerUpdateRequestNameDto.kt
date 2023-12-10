package io.koosha.foobar.customer.api.model.dto

import io.koosha.foobar.customer.api.model.Title
import io.koosha.foobar.customer.api.model.FIRST_NAME_MAX_LEN
import io.koosha.foobar.customer.api.model.LAST_NAME_MAX_LEN
import jakarta.validation.constraints.Size

data class CustomerUpdateRequestNameDto(
    val title: Title?,

    @field:Size(min = 1, max = FIRST_NAME_MAX_LEN)
    val firstName: String?,

    @field:Size(min = 1, max = LAST_NAME_MAX_LEN)
    val lastName: String?,
)
