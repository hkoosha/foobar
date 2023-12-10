package io.koosha.foobar

object Libraries {

    object Kotlin {
        const val jvm = "1.9.21" // held back by detekt.
        const val spring = jvm
        const val jpa = jvm

        const val detekt = "1.23.4"
    }

    object Spring {
        const val dependencyManagement = "1.1.4"
        const val springCloudVersion = "2023.0.0"
        const val boot = "3.2.1"
    }

    object Proto {
        const val protoPlugin = "0.9.4"
        const val proto = "3.25.1"
    }

    object Kafka {
        const val kafka = "3.6.1"
    }

    object OpenApi {
        const val ui = "2.3.0"
        const val gradlePlugin = "1.8.0"
        const val jacksonNullable = "0.2.6"
    }

    object Feign {
        const val core = "13.1"
    }

    object Jackson {
        const val core = "2.16.0"
    }

    object Jib {
        const val gradlePlugin = "3.4.0"
    }

    object Log {
        const val logstashLogbackEncoder = "7.4"
    }

    object Reactor {
        const val core = "3.6.0"
        const val nettyHttp = "1.1.13"
    }

    const val postgresR2dbc = "1.0.3.RELEASE"
    const val postgres = "42.7.1"

    const val flyway = "10.4.1"
    const val flywayPg = flyway


    const val javaxAnnotation = "1.3.2"

}
