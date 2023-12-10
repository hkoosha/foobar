@file:Suppress("RemoveRedundantQualifierName")

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.koosha.foobar.Foobar
import io.koosha.foobar.Libraries
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val s = io.koosha.foobar.Libraries.Spring
    val k = io.koosha.foobar.Libraries.Kotlin

    kotlin("jvm") version k.jvm
    kotlin("plugin.spring") version k.spring

    id("org.springframework.boot") version s.boot
    id("io.spring.dependency-management") version s.dependencyManagement

    id("io.gitlab.arturbosch.detekt") version k.detekt
}

group = Foobar.group
version = Foobar.appVersion

java {
    sourceCompatibility = JavaVersion.valueOf(Foobar.javaVersion)
    targetCompatibility = JavaVersion.valueOf(Foobar.javaVersion)
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

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    implementation("javax.annotation:javax.annotation-api:${Libraries.javaxAnnotation}")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
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
