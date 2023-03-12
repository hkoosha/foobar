@file:Suppress("RemoveRedundantQualifierName")

import io.koosha.foobar.Foobar

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
        classpath("org.openapitools:openapi-generator:6.4.0")
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
        val content = toFix.readText()
        toFix.delete()
        toFix.createNewFile()
        toFix.writeText(Foobar.fixApiBuilder(content, flux = true))
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
