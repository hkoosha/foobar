@file:Suppress("RemoveRedundantQualifierName")

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.koosha.foobar.Foobar
import io.koosha.foobar.Libraries
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    val j = io.koosha.foobar.Libraries.Jib
    val s = io.koosha.foobar.Libraries.Spring
    val k = io.koosha.foobar.Libraries.Kotlin

    id("io.gitlab.arturbosch.detekt") version k.detekt
    id("com.google.cloud.tools.jib") version j.gradlePlugin
    id("org.springframework.boot") version s.boot
    id("io.spring.dependency-management") version s.DependencyManagement
    kotlin("jvm") version k.jvm
    kotlin("plugin.spring") version k.spring
    kotlin("plugin.jpa") version k.jpa
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
    implementation(project(":common-jpa"))
    implementation(project(":common-meter"))

    implementation(project(":connect:customer-api-build"))
    implementation(project(":connect:seller-api-build"))
    implementation(project(":connect:warehouse-api-build"))
    implementation(project(":connect:marketplace-api-build"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")

    implementation("io.github.microutils:kotlin-logging-jvm:${Libraries.microutilsKotlinLoggingJvm}")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:${Libraries.Jackson.core}")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("io.github.openfeign:feign-micrometer")
    runtimeOnly("io.github.openfeign:feign-okhttp")
    runtimeOnly("com.squareup.okhttp3:okhttp")
    runtimeOnly("org.postgresql:postgresql:${Libraries.postgres}")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:${Libraries.mariadb}")
    runtimeOnly("com.h2database:h2")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

jib {
    val foobarCmd = "'/maker.sh loop c:customer c:addr c:seller c:prod c:avail c:ord c:line u:ord'"
    setAllowInsecureRegistries(true)
    extraDirectories.setPaths(
        "${project.projectDir}/extra",
    )
    container {
        entrypoint = listOf(
            "bash",
            "-c",
            "chmod +x /maker.sh && echo alias maker=/maker.sh >> ~/.bashrc && echo alias foobar=\"$foobarCmd\" >> ~/.bashrc && sleep infinity",
        )
    }
    to {
        image = "${Foobar.dockerRegistry()}foobar-maker:${Foobar.appVersion}"
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = Foobar.kotlinJvmTarget
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = Foobar.kotlinJvmTarget
}
