@file:Suppress("RemoveRedundantQualifierName")

import io.koosha.foobar.Foobar
import io.koosha.foobar.Libraries

plugins {
    val k = io.koosha.foobar.Libraries.Kotlin
    val d = io.koosha.foobar.Libraries.OpenApi

    id("org.openapi.generator") version d.generatorPlugin
    kotlin("jvm") version k.jvm
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.openapitools:openapi-generator:6.0.1")
    }
}


group = Foobar.group
version = Foobar.appVersion
java.sourceCompatibility = JavaVersion.valueOf(Foobar.javaVersion)
java.targetCompatibility = JavaVersion.valueOf(Foobar.javaVersion)

repositories {
    Foobar.addRepositories(this)
}

dependencies {
    compileOnly(project(":service:warehouse"))
}

val foobarCleanTask = "foobar-clean-api-build"

tasks.register<Delete>(Foobar.Gradle.foobarCleanApiBuildTaskName) {
    delete.add(file("$projectDir/../rx-warehouse-api-build"))
}

tasks.named("openApiGenerate") {
    dependsOn(tasks.named(Foobar.Gradle.foobarCleanApiBuildTaskName))
    dependsOn(project(":service:warehouse").tasks.named("generateOpenApiDocs"))

    // TODO find a way to override webclient's build.gradle mustache template.
    doLast {
        val toFix = file("$projectDir/../rx-warehouse-api-build/build.gradle")
        val lines = toFix
            .readLines()
            .map {
                it
                    .replace(
                        "main = System.getProperty('mainClass')",
                        "mainClass.set(System.getProperty('mainClass'))"
                    )
            }
        var inExt = false
        val newContent = StringBuilder()
        val versionReplacements = mapOf(
            "swagger_annotations_version" to Libraries.OpenApi.swaggerAnnotations,
            "spring_boot_version" to Libraries.Spring.springBoot2,
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
        toFix.delete()
        toFix.createNewFile()
        toFix.writeText(newContent.toString())
    }
}

openApiGenerate {
    val warehouseDir = project(":service:warehouse").projectDir
    val foobarPackage = "io.koosha.foobar.connect.warehouse.rx.generated.api"

    apiPackage.set(foobarPackage)
    modelPackage.set(foobarPackage)
    generatorName.set("java")
    library.set("webclient")
    inputSpec.set("$warehouseDir/${Foobar.OpenApi.projectRelativeFileName}")
    outputDir.set("$projectDir/../rx-warehouse-api-build")
    configFile.set("$projectDir/src/main/resources/api.json")
}
