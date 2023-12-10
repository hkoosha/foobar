package io.koosha.foobar.customer.api.model.dto

import io.koosha.foobar.customer.api.model.ADDRESS_ADDRESS_LINE1_MAX_LEN
import io.koosha.foobar.customer.api.model.ADDRESS_CITY_MAX_LEN
import io.koosha.foobar.customer.api.model.ADDRESS_COUNTRY_MAX_LEN
import io.koosha.foobar.customer.api.model.ADDRESS_NAME_MAX_LEN
import io.koosha.foobar.customer.api.model.ADDRESS_ZIPCODE_MAX_LEN
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CustomerAddressCreateRequestDto(
    @field:NotNull
    @field:Size(min = 1, max = ADDRESS_ZIPCODE_MAX_LEN)
    val zipcode: String?,

    @field:NotNull
    @field:Size(min = 1, max = ADDRESS_COUNTRY_MAX_LEN)
    val country: String?,

    @field:NotNull
    @field:Size(min = 1, max = ADDRESS_CITY_MAX_LEN)
    val city: String?,

    @field:NotNull
    @field:Size(min = 1, max = ADDRESS_ADDRESS_LINE1_MAX_LEN)
    val addressLine1: String?,

    @field:NotNull
    @field:Size(min = 1, max = ADDRESS_NAME_MAX_LEN)
    val name: String?,
)
