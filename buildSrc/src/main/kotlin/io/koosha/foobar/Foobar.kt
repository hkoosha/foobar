package io.koosha.foobar

import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler


object Foobar {

    private fun getEnv(
        project: Project?,
        name: String,
        defaultValue: String? = null,
    ): String {
        val proj = project?.findProperty(name) as String?
        if (proj != null)
            return proj

        val prop = System.getProperty(name)
        if (prop != null)
            return prop

        val env = System.getenv(name)
        if (env != null)
            return env

        if (defaultValue != null)
            return defaultValue

        throw GradleException("$name not set")
    }

    object OpenApi {

        const val fileName = "open-api.yaml"
        const val projectRelativePath = "src/main/resources"
        const val projectRelativeFileName = "$projectRelativePath/open-api.yaml"
        const val httpRelativePaht = "api.yaml"

    }

    object Port {

        fun customer(project: Project?) = getEnv(project, "FOOBAR_SERVICE_PORT_CUSTOMER", "4040")

        fun marketplace(project: Project?) = getEnv(project, "FOOBAR_SERVICE_PORT_MARKETPLACE", "4041")

        fun seller(project: Project?) = getEnv(project, "FOOBAR_SERVICE_PORT_SELLER", "4043")

        fun shipping(project: Project?) = getEnv(project, "FOOBAR_SERVICE_PORT_SHIPPING", "4044")

        fun warehouse(project: Project?) = getEnv(project, "FOOBAR_SERVICE_PORT_WAREHOUSE", "4046")
    }

    object Gradle {

        const val foobarCleanOpenApiTask = "foobar-clean-open-api"

    }

    object Boot {

        fun jvmArgs(project: Project) = listOf(
            "-javaagent:${project.rootDir}/libs/opentelemetry-javaagent-1.32.0.jar",
            "-Dotel.service.name=${project.name}",
            "-Dotel.traces.sampler=always_on",
            "-Dotel.traces.sampler=parentbased_traceidratio",
            // "-Dotel.traces.sampler.arg=0.1",
        )

    }

    object Jib {

        fun extraDirs(project: Project) = listOf(
            "${project.rootDir}/libs",
        )

        fun jvmFlags(project: Project) = listOf(
            "-javaagent:/opentelemetry-javaagent-1.32.0.jar",
            "-Dotel.service.name=${project.name}",
            // "-Dotel.traces.sampler=always_on",
            "-Dotel.traces.sampler=parentbased_traceidratio",
            // TODO move ratio to env var
            "-Dotel.traces.sampler.arg=0.1",
            "-Dotel.traces.exporter=jaeger",
        )

        const val fromImage = "eclipse-temurin:21-jre"

    }

    const val javaVersion = "VERSION_21"
    const val kotlinJvmTarget = "21"
    const val appVersion = "0.0.1-SNAPSHOT"
    const val group = "io.koosha.foobar"

    val kotlinFreeCompilerArgs = listOf(
        // https://kotlinlang.org/docs/reference/whatsnew13.html#progressive-mode
        "-progressive",

        // https://github.com/Kotlin/KEEP/blob/master/proposals/jsr-305-custom-nullability-qualifiers.md
        "-Xjsr305=strict",
    )

    fun addRepositories(repositoryHandler: RepositoryHandler) {
        repositoryHandler.mavenCentral()
        repositoryHandler.maven {
            setUrl("https://plugins.gradle.org/m2/")
        }
    }

    fun dockerRegistry(): String {

        val registry: String = System.getenv("FOOBAR_DOCKER_REGISTRY")?.trim()
            ?: return ""

        if (!registry.endsWith("/"))
            throw GradleException("foobar docker registry (env var: FOOBAR_DOCKER_REGISTRY) " +
                    "must end with a slash, actual value: $registry")

        return registry
    }

}
