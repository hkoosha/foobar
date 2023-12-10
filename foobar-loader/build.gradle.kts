@file:Suppress("RemoveRedundantQualifierName")

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.koosha.foobar.Foobar
import io.koosha.foobar.Libraries
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    val j = io.koosha.foobar.Libraries.Jib
    val s = io.koosha.foobar.Libraries.Spring
    val k = io.koosha.foobar.Libraries.Kotlin

    kotlin("jvm") version k.jvm
    kotlin("plugin.spring") version k.spring

    id("io.gitlab.arturbosch.detekt") version k.detekt

    id("com.google.cloud.tools.jib") version j.gradlePlugin

    id("org.springframework.boot") version s.boot
    id("io.spring.dependency-management") version s.dependencyManagement
}

java {
    group = Foobar.group
    version = Foobar.appVersion
    sourceCompatibility = JavaVersion.valueOf(Foobar.javaVersion)
    targetCompatibility = JavaVersion.valueOf(Foobar.javaVersion)
}

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

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

    runtimeOnly("io.github.openfeign:feign-okhttp")
    runtimeOnly("com.squareup.okhttp3:okhttp")

    implementation("net.logstash.logback:logstash-logback-encoder:${Libraries.Log.logstashLogbackEncoder}")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Libraries.Jackson.core}")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")

    runtimeOnly("org.postgresql:postgresql:${Libraries.postgres}")
    runtimeOnly("com.h2database:h2")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<BootRun> {
    jvmArgs = Foobar.Boot.jvmArgs(project)
}

jib {
    setAllowInsecureRegistries(true)
    extraDirectories.setPaths(
        Foobar.Jib.extraDirs(project)
    )
    container {
        jvmFlags = Foobar.Jib.jvmFlags(project)
    }
    from {
        image = Foobar.Jib.fromImage
    }
    to {
        image = "${Foobar.dockerRegistry()}foobar-loader:${Foobar.appVersion}"
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = Foobar.kotlinJvmTarget
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = Foobar.kotlinJvmTarget
}
