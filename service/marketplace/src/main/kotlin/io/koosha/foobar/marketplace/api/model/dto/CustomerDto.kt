package io.koosha.foobar.marketplace.api.model.dto

import java.util.UUID

data class CustomerDto(
    val customerId: UUID,
    val isActive: Boolean,
)
