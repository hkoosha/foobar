package io.koosha.foobar.customer.api.service

import io.koosha.foobar.customer.api.model.Title
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


internal const val FIRST_NAME_MAX_LEN = 127
internal const val LAST_NAME_MAX_LEN = 127
internal const val ADDRESS_ZIPCODE_MAX_LEN = 10
internal const val ADDRESS_CITY_MAX_LEN = 127
internal const val ADDRESS_COUNTRY_MAX_LEN = 127
internal const val ADDRESS_ADDRESS_LINE1_MAX_LEN = 127
internal const val ADDRESS_NAME_MAX_LEN = 127


data class CustomerCreateRequest(

    @field:Valid
    @field:NotNull
    val name: CustomerCreateRequestName?,
)

data class CustomerCreateRequestName(

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


data class CustomerUpdateRequest(

    @field:Valid
    val name: CustomerUpdateRequestName? = null,
)

data class CustomerUpdateRequestName(

    val title: Title?,

    @field:Size(min = 1, max = FIRST_NAME_MAX_LEN)
    val firstName: String?,

    @field:Size(min = 1, max = LAST_NAME_MAX_LEN)
    val lastName: String?,
)


data class CustomerAddressCreateRequest(

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
