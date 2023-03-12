@file:Suppress("RemoveRedundantQualifierName")

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.koosha.foobar.Foobar
import io.koosha.foobar.Libraries
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val s = io.koosha.foobar.Libraries.Spring
    val k = io.koosha.foobar.Libraries.Kotlin

    id("io.gitlab.arturbosch.detekt") version k.detekt
    id("org.springframework.boot") version s.boot
    id("io.spring.dependency-management") version s.DependencyManagement
    kotlin("jvm") version k.jvm
    kotlin("plugin.spring") version k.spring
}

group = Foobar.group
version = Foobar.appVersion
java.sourceCompatibility = JavaVersion.valueOf(Foobar.javaVersion)
java.targetCompatibility = JavaVersion.valueOf(Foobar.javaVersion)

extra["springCloudVersion"] = Libraries.Spring.springCloudVersion

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    Foobar.addRepositories(this)
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = Foobar.kotlinFreeCompilerArgs
        jvmTarget = Foobar.kotlinJvmTarget
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Libraries.Spring.springCloudVersion}")
    }
}

dependencies {
    implementation(project(":common"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("io.github.microutils:kotlin-logging-jvm:${Libraries.microutilsKotlinLoggingJvm}")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("javax.annotation:javax.annotation-api:${Libraries.javaxAnnotation}")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = Foobar.kotlinJvmTarget
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = Foobar.kotlinJvmTarget
}

tasks.bootRun {
    enabled = false
}

tasks.bootJar {
    enabled = false
}

