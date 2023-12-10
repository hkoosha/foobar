package io.koosha.foobar.marketplace.api.model.dto

import java.util.UUID

data class SellerDto(
    val name: String,
    val sellerId: UUID,
    val isActive: Boolean,
)
