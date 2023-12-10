package io.koosha.foobar.common

import org.springframework.stereotype.Component
import java.util.*

fun String.toUUID(): UUID = UUID.fromString(this)

interface RandomUUIDProvider {

    fun randomUUID(): UUID

}

@Component
class DefaultRandomUUIDProvider : RandomUUIDProvider {

    override fun randomUUID(): UUID = UUID.randomUUID()

}
