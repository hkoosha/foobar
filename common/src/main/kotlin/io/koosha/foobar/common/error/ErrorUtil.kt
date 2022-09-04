package io.koosha.foobar.common.error


fun anyOfMessagesContains(ex: Throwable, msg: String): Boolean {

    var e: Throwable? = ex
    while (e != null)
        if (e.message?.contains(msg) == true)
            return true
        else
            e = e.cause

    return false
}
