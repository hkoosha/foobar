package io.koosha.foobar.seller.api.model.dto

import io.koosha.foobar.seller.api.model.NAME_MAX_LEN
import jakarta.validation.Valid
import jakarta.validation.constraints.Size

data class SellerUpdateRequestDto(
    @field:Size(min = 1, max = NAME_MAX_LEN)
    val name: String? = null,

    @field:Valid
    val address: SellerUpdateRequestAddressDto? = null,
)
