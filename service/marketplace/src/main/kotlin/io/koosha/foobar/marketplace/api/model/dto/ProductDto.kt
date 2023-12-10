package io.koosha.foobar.marketplace.api.model.dto

import java.util.UUID

data class ProductDto(
    val productId: UUID,
    val active: Boolean,
    val name: String,
    val unitMultiple: String,
    val unitSingle: String,
)
