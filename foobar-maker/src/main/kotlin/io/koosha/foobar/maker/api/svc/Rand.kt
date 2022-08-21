package io.koosha.foobar.maker.api.svc

import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom


@Service
class Rand {

    private fun r(): ThreadLocalRandom = ThreadLocalRandom.current()

    fun string(len: Int): String = this.string(len, "")

    fun string(len: Int, prefix: String): String {

        val buffer = StringBuilder(len)
        repeat(len) {
            buffer.append(('a'.code + r().nextInt(0, 'z'.code - 'a'.code + 1)).toChar())
        }

        return prefix + buffer.toString()
    }

    fun <T> select(vararg ts: T): T = ts[this.r().nextInt(0, ts.size)]

    fun <T : Enum<T>> selectEnum(e: Class<T>): T {
        val constants: Array<T> = e.enumConstants ?: throw IllegalArgumentException("not an enum: ${e.name}")
        return constants[this.r().nextInt(0, constants.size)]
    }

    fun long(max: Long = 100, min: Long = 0): Long = this.r().nextLong(min, max)

    fun int(max: Int = 100, min: Int = 0): Int = this.r().nextInt(min, max)

}
