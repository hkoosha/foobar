package io.koosha.foobar

import org.gradle.internal.file.impl.DefaultFileMetadata.file

object Libraries {

    object Kotlin {
        const val jvm = "1.7.10"
        const val spring = jvm
        const val jpa = jvm

        const val detekt = "1.21.0"
    }

    object Spring {
        const val DependencyManagement = "1.0.13.RELEASE"
        const val springCloudVersion = "2021.0.3"
        const val springCloudSleuthOtelVersion = "1.1.0-M6"
        const val springBoot2 = "2.7.3"
        const val retry = "1.3.3"
    }

    // object Test {
    //     const val jupiterApi = "5.9.0"
    // }

    object Proto {
        const val protoPlugin = "0.8.19"
        const val proto = "3.21.5"
    }

    object Kafka {
        const val kafka = "3.2.1"
    }

    object OpenApi {
        const val ui = "1.6.10"
        const val kotlin = ui
        const val rest = ui
        const val webMvcCore = ui
        const val gradlePlugin = "1.3.4"
        const val generatorPlugin = "6.0.1"
        // const val swaggerAnnotationVersion = "2.2.1"
        const val jacksonNullable = "0.2.3"
    }

    object Feign {
        const val core = "11.9.1"
    }

    object Jackson {
        const val core = "2.13.3"
    }

    object Jib {
        const val gradlePlugin = "3.2.1"
    }

    object Log {
        // const val logbackJackson = "0.1.5"
        const val logbackJsonClassic = "0.1.5"
        const val logstashLogbackEncoder = "7.2"
    }

    const val mariadb = "3.0.7"
    const val postgres = "42.4.2"

    const val jakartaAnnotationApi = "2.1.1"
    const val javaxAnnotation = "1.3.2"
    const val microutilsKotlinLoggingJvm = "2.1.23"
}
