package io.koosha.foobar.common

import org.springframework.dao.DataIntegrityViolationException


fun isDuplicateEntry(it: Throwable): Boolean {

    if (it !is DataIntegrityViolationException)
        return false

    return it.cause?.message?.contains("Duplicate entry") == true ||
            it.message?.contains("Duplicate entry") == true
}
