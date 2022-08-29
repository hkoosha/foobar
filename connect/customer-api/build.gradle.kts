import io.koosha.foobar.Foobar

plugins {
    val d = io.koosha.foobar.Libraries.OpenApi
    val k = io.koosha.foobar.Libraries.Kotlin

    id("org.openapi.generator") version d.generatorPlugin
    kotlin("jvm") version k.jvm
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.openapitools:openapi-generator:6.0.1")
        classpath(files("../../libs/foobar-gen.jar"))
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
    compileOnly(project(":service:customer"))
}

tasks.register<Delete>(Foobar.Gradle.foobarCleanApiBuildTaskName) {
    delete.add(file("$projectDir/../customer-api-build"))
}

tasks.named("openApiGenerate") {
    dependsOn(tasks.named(Foobar.Gradle.foobarCleanApiBuildTaskName))
    dependsOn(project(":service:customer").tasks.named("generateOpenApiDocs"))
}

openApiGenerate {
    val customerDir = project(":service:customer").projectDir
    val foobarPackage = "io.koosha.foobar.connect.customer.generated.api"

    apiPackage.set(foobarPackage)
    modelPackage.set(foobarPackage)
    generatorName.set("io.koosha.foobar.gen.FoobarGenGenerator")
    library.set("feign")
    inputSpec.set("$customerDir/${Foobar.OpenApi.projectRelativeFileName}")
    outputDir.set("$projectDir/../customer-api-build")
    configFile.set("$projectDir/src/main/resources/api.json")
}
