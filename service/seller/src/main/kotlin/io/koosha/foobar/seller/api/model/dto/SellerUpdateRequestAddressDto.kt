package io.koosha.foobar.seller.api.model.dto

import io.koosha.foobar.seller.api.model.ADDRESS_ADDRESS_LINE1_MAX_LEN
import io.koosha.foobar.seller.api.model.ADDRESS_CITY_MAX_LEN
import io.koosha.foobar.seller.api.model.ADDRESS_COUNTRY_MAX_LEN
import io.koosha.foobar.seller.api.model.ADDRESS_ZIPCODE_MAX_LEN
import jakarta.validation.constraints.Size

data class SellerUpdateRequestAddressDto(
    @field:Size(min = 1, max = ADDRESS_ZIPCODE_MAX_LEN)
    val zipcode: String? = null,

    @field:Size(min = 1, max = ADDRESS_ADDRESS_LINE1_MAX_LEN)
    val addressLine1: String? = null,

    @field:Size(min = 1, max = ADDRESS_COUNTRY_MAX_LEN)
    val country: String? = null,

    @field:Size(min = 1, max = ADDRESS_CITY_MAX_LEN)
    val city: String? = null,
)
