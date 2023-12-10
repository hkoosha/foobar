package io.koosha.foobar.shipping.api.model

enum class ShippingState(
    val deletionAllowed: Boolean,
) {

    ON_WAY_TO_CUSTOMER(false),
    DELIVERED(true),
    ;

}
