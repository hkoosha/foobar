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
        const val foobarCleanApiBuildTaskName = "foobar-clean-api-build"

    }

    object Boot {

        fun jvmArgs(project: Project) = listOf(
            "-javaagent:${project.rootDir}/libs/opentelemetry-javaagent-1.27.0.jar",
            "-Dotel.service.name=${project.name}",
            // "-Dotel.traces.sampler=always_on",
            "-Dotel.traces.sampler=parentbased_traceidratio",
            "-Dotel.traces.sampler.arg=0.1",
            "-Dlogback.debug=true"
        )

    }

    object Jib {

        fun extraDirs(project: Project) = listOf(
            "${project.rootDir}/libs",
        )

        fun jvmFlags(project: Project) = listOf(
            "-javaagent:/opentelemetry-javaagent-1.27.0.jar",
            "-Dotel.service.name=${project.name}",
            // "-Dotel.traces.sampler=always_on",
            "-Dotel.traces.sampler=parentbased_traceidratio",
            // TODO move ratio to env var
            "-Dotel.traces.sampler.arg=0.1",
            "-Dotel.traces.exporter=jaeger",
        )

    }

    const val javaVersion = "VERSION_17"
    const val kotlinJvmTarget = "17"
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

        val registry = System.getenv("FOOBAR_DOCKER_REGISTRY")?.trim()
        if (registry == null)
            return ""

        if (!registry.endsWith("/"))
            throw GradleException("foobar docker registry (env var: FOOBAR_DOCKER_REGISTRY) must end with a slash, actual value: $registry")
        return registry
    }

    fun fixApiBuilder(toFix: String, flux: Boolean = false): String {
        val lines = toFix
            .lines()
            .map {
                it
                    .replace(
                        "main = System.getProperty('mainClass')",
                        "mainClass.set(System.getProperty('mainClass'))",
                    )
                    .replace(
                        "classifier = 'sources'",
                        "archiveClassifier = 'sources'",
                    )
                    .replace(
                        "classifier = 'javadoc'",
                        "archiveClassifier = 'javadoc'",
                    )
                    .replace(
                        "apply plugin: 'eclipse'",
                        "apply plugin: 'java'",
                    )
                    .replace(
                        "version = 'v0'",
                        """
                        version = 'v0'
                        java.sourceCompatibility = JavaVersion.VERSION_17
                        java.targetCompatibility = JavaVersion.VERSION_17
                        java.disableAutoTargetJvm()
                        """.trimIndent(),
                    )
            }
        var inExt = false
        val newContent = StringBuilder()
        val versionReplacements = mapOf(
            "swagger_annotations_version" to Libraries.OpenApi.swaggerAnnotations,
            "spring_boot_version" to when(Libraries.Spring.boot) {
                "3.0.4" ->
                    if(flux)
                        "3.0.3" // not released as of writing this.
                    else
                        Libraries.Spring.boot
                else -> Libraries.Spring.boot
            },
            "jackson_version" to Libraries.Jackson.core,
            "jackson_databind_version" to Libraries.Jackson.core,
            "jackson_databind_nullable_version" to Libraries.OpenApi.jacksonNullable,
            "jakarta_annotation_version" to Libraries.jakartaAnnotationApi,
            "reactor_version" to Libraries.Reactor.core,
            "reactor_netty_version" to Libraries.Reactor.nettyHttp,
        )
        for (line in lines) {
            if (line == "ext {") {
                inExt = true
                newContent.append("\n").append(line)
            }
            else if (inExt && line == "}") {
                inExt = false
                newContent.append("\n").append(line)
            }
            else if (!inExt) {
                newContent.append("\n").append(line)
            }
            else {
                var anyReplacement = false
                for ((artifact, versionReplacement) in versionReplacements)
                    if (line.contains(" $artifact =")) {
                        newContent.append("\n    $artifact = \"$versionReplacement\"")
                        anyReplacement = true
                        break
                    }
                if (!anyReplacement) {
                    newContent.append("\n").append(line)
                }
            }
        }

        return newContent.toString()
    }
}
