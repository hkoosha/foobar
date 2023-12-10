package io.koosha.foobar.shipping.api.model.dto

data class AddressDto(
    val zipcode: String,
    val addressLine1: String,
    val country: String,
    val city: String,
)
