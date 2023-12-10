package io.koosha.foobar.warehouse.api.model.dto

import io.koosha.foobar.warehouse.api.model.PRODUCT_MULTIPLE_UNIT_MAX_LEN
import io.koosha.foobar.warehouse.api.model.PRODUCT_NAME_MAX_LEN
import io.koosha.foobar.warehouse.api.model.PRODUCT_SINGLE_UNIT_MAX_LEN
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class ProductCreateRequestDto(
    @field:NotNull
    val active: Boolean?,

    @field:NotNull
    @field:Size(min = 1, max = PRODUCT_NAME_MAX_LEN)
    val name: String?,

    @field:NotNull
    @field:Size(min = 1, max = PRODUCT_SINGLE_UNIT_MAX_LEN)
    val unitSingle: String?,

    @field:NotNull
    @field:Size(min = 1, max = PRODUCT_MULTIPLE_UNIT_MAX_LEN)
    val unitMultiple: String?,
)
