package io.koosha.foobar.shipping.api.model.dto

import java.util.UUID

data class SellerDto(
    val address: AddressDto,
    val name: String,
    val sellerId: UUID,
    val isActive: Boolean,
)
