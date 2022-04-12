plugins {
    val k = io.koosha.foobar.Libraries.Kotlin

    idea
    kotlin("jvm") version k.jvm apply false
}