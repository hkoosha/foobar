package io.koosha.foobar.seller.api.model.dto

import io.koosha.foobar.seller.api.model.NAME_MAX_LEN
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class SellerCreateRequestDto(
    @field:NotNull
    @field:Size(min = 1, max = NAME_MAX_LEN)
    val name: String?,

    @field:Valid
    @field:NotNull
    val address: SellerCreateRequestAddressDto?,
)
