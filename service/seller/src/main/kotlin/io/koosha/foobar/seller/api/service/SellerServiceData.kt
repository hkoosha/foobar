package io.koosha.foobar.seller.api.service

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size


internal const val NAME_MAX_LEN = 127
internal const val ADDRESS_ZIPCODE_MAX_LEN = 10
internal const val ADDRESS_ADDRESS_LINE1_MAX_LEN = 127
internal const val ADDRESS_CITY_MAX_LEN = 127
internal const val ADDRESS_COUNTRY_MAX_LEN = 127


data class SellerCreateRequestAddress(

    @field:NotNull
    @field:Size(min = 1, max = ADDRESS_ZIPCODE_MAX_LEN)
    val zipcode: String?,

    @field:NotNull
    @field:Size(min = 1, max = ADDRESS_ADDRESS_LINE1_MAX_LEN)
    val addressLine1: String?,

    @field:NotNull
    @field:Size(min = 1, max = ADDRESS_COUNTRY_MAX_LEN)
    val country: String?,

    @field:NotNull
    @field:Size(min = 1, max = ADDRESS_CITY_MAX_LEN)
    val city: String?,
)

data class SellerCreateRequest(

    @field:NotNull
    @field:Size(min = 1, max = NAME_MAX_LEN)
    val name: String?,

    @field:Valid
    @field:NotNull
    val address: SellerCreateRequestAddress?,
)


data class SellerUpdateRequestAddress(

    @field:Size(min = 1, max = ADDRESS_ZIPCODE_MAX_LEN)
    val zipcode: String? = null,

    @field:Size(min = 1, max = ADDRESS_ADDRESS_LINE1_MAX_LEN)
    val addressLine1: String? = null,

    @field:Size(min = 1, max = ADDRESS_COUNTRY_MAX_LEN)
    val country: String? = null,

    @field:Size(min = 1, max = ADDRESS_CITY_MAX_LEN)
    val city: String? = null,
)

data class SellerUpdateRequest(

    @field:Size(min = 1, max = NAME_MAX_LEN)
    val name: String? = null,

    @field:Valid
    val address: SellerUpdateRequestAddress? = null,
)
