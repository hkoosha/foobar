package io.koosha.foobar.marketplace.api.model


enum class OrderRequestState(val deletionAllowed: Boolean) {

    // Line items are being added
    ACTIVE(true),

    // Finished adding live items, request is frozen, looking for seller
    LIVE(false),

    NO_SELLER_FOUND(true),

    WAITING_FOR_SELLER(false),

    FULFILLED(true),

    ;

}
