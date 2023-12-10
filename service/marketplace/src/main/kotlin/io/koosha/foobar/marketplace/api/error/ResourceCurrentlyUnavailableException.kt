package io.koosha.foobar.marketplace.api.error

class ResourceCurrentlyUnavailableException : Exception {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(cause: Throwable) : super(cause)

    constructor(
        message: String,
        cause: Throwable,
    ) : super(message, cause)

}
