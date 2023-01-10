package io.koosha.foobar

import org.gradle.internal.file.impl.DefaultFileMetadata.file

object Libraries {

    object Kotlin {
        const val jvm = "1.8.0"
        const val spring = jvm
        const val jpa = jvm

        const val detekt = "1.22.0"
    }

    object Spring {
        const val DependencyManagement = "1.0.15.RELEASE"
        const val springCloudVersion = "2021.0.5"
        const val springBoot2 = "2.7.7"
        const val retry = "2.0.0"
    }

    object Proto {
        const val protoPlugin = "0.9.1"
        const val proto = "3.21.12"
    }

    object Kafka {
        const val kafka = "3.3.1"
    }

    object OpenApi {
        const val ui = "1.6.14"
        const val kotlin = ui
        const val rest = ui
        const val webMvcCore = ui
        const val gradlePlugin = "1.6.0"
        const val generatorPlugin = "6.2.1"
        const val jacksonNullable = "0.2.4"
        const val swaggerAnnotations = "1.6.9"
    }

    object Feign {
        const val core = "12.1"
    }

    object Jackson {
        const val core = "2.14.1"
    }

    object Jib {
        const val gradlePlugin = "3.3.1"
    }

    object Log {
        const val logbackJsonClassic = "0.1.5"
        const val logstashLogbackEncoder = "7.2"
    }

    object Reactor {
        const val core = "3.5.1"
        const val nettyHttp = "1.1.1"
    }

    const val mariadb = "3.1.0"
    const val postgres = "42.5.1"
    const val flyway = "9.11.0" // "8.2.0"

    const val jakartaAnnotationApi = "1.3.5"
    const val javaxAnnotation = "1.3.2"
    const val microutilsKotlinLoggingJvm = "3.0.4"

}
