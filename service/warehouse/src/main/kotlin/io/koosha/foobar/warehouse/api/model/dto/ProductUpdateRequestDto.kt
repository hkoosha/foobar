package io.koosha.foobar.warehouse.api.model.dto

import io.koosha.foobar.warehouse.api.model.PRODUCT_MULTIPLE_UNIT_MAX_LEN
import io.koosha.foobar.warehouse.api.model.PRODUCT_NAME_MAX_LEN
import io.koosha.foobar.warehouse.api.model.PRODUCT_SINGLE_UNIT_MAX_LEN
import jakarta.validation.constraints.Size

data class ProductUpdateRequestDto(
    val active: Boolean? = null,

    @field:Size(min = 1, max = PRODUCT_NAME_MAX_LEN)
    val name: String? = null,

    @field:Size(min = 1, max = PRODUCT_SINGLE_UNIT_MAX_LEN)
    val unitSingle: String? = null,

    @field:Size(min = 1, max = PRODUCT_MULTIPLE_UNIT_MAX_LEN)
    val unitMultiple: String? = null,
)
