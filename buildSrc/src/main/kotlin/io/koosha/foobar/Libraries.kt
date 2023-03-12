package io.koosha.foobar

object Libraries {

    object Kotlin {
        const val jvm = "1.8.10"
        const val spring = jvm
        const val jpa = jvm

        const val detekt = "1.22.0"
    }

    object Spring {
        const val DependencyManagement = "1.1.0"
        const val springCloudVersion = "2022.0.1"
        const val boot = "3.0.4"
        const val retry = "2.0.0"
    }

    object Proto {
        const val protoPlugin = "0.9.2"
        const val proto = "3.22.0"
    }

    object Kafka {
        const val kafka = "3.4.0"
    }

    object OpenApi {
        const val ui = "2.0.2"
        const val kotlin = "1.6.14"
        const val rest = ui
        const val webMvcCore = ui
        const val gradlePlugin = "1.6.0"
        const val generatorPlugin = "6.4.0"
        const val jacksonNullable = "0.2.6"
        const val swaggerAnnotations = "1.6.9"
    }

    object Feign {
        const val core = "12.2"
    }

    object Jackson {
        const val core = "2.14.2"
    }

    object Jib {
        const val gradlePlugin = "3.3.1"
    }

    object Log {
        const val logstashLogbackEncoder = "7.3"
    }

    object Reactor {
        const val core = "3.5.3"
        const val nettyHttp = "1.1.4"
    }

    const val postgresR2dbc = "1.0.1.RELEASE"
    const val mariadbR2dbc = "1.1.3"
    const val mariadb = "3.1.2"
    const val postgres = "42.5.4"
    const val flyway = "9.15.2"

    const val jakartaAnnotationApi = "1.3.5"
    const val javaxAnnotation = "1.3.2"
    const val microutilsKotlinLoggingJvm = "3.0.5"

}
