package io.koosha.foobar.seller.api.model

import io.koosha.foobar.seller.api.model.dto.SellerUpdateRequestAddressDto
import jakarta.validation.Valid
import jakarta.validation.constraints.Size


internal const val NAME_MAX_LEN = 127
internal const val ADDRESS_ZIPCODE_MAX_LEN = 10
internal const val ADDRESS_ADDRESS_LINE1_MAX_LEN = 127
internal const val ADDRESS_CITY_MAX_LEN = 127
internal const val ADDRESS_COUNTRY_MAX_LEN = 127
