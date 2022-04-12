package io.koosha.foobar.warehouse.api.service

import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


const val PRODUCT_NAME_MAX_LEN = 127
const val PRODUCT_SINGLE_UNIT_MAX_LEN = 10
const val PRODUCT_MULTIPLE_UNIT_MAX_LEN = 10


data class ProductCreateRequest(

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

data class ProductUpdateRequest(

    val active: Boolean? = null,

    @field:Size(min = 1, max = PRODUCT_NAME_MAX_LEN)
    val name: String? = null,

    @field:Size(min = 1, max = PRODUCT_SINGLE_UNIT_MAX_LEN)
    val unitSingle: String? = null,

    @field:Size(min = 1, max = PRODUCT_MULTIPLE_UNIT_MAX_LEN)
    val unitMultiple: String? = null,
)


data class AvailabilityCreateRequest(

    @field:NotNull
    val sellerId: UUID?,

    @field:Min(0)
    @field:NotNull
    val unitsAvailable: Long?,

    @field:Min(0)
    @field:NotNull
    val pricePerUnit: Long?,
)

data class AvailabilityUpdateRequest(

    @field:Min(0)
    val unitsAvailable: Long?,

    @field:Min(0)
    val unitsToFreeze: Long?,

    @field:Min(0)
    val pricePerUnit: Long?,
)
