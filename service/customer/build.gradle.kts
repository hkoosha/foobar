import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import io.koosha.foobar.Foobar
import io.koosha.foobar.Libraries
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springdoc.openapi.gradle.plugin.OpenApiGeneratorTask
import org.springframework.boot.gradle.tasks.run.BootRun

plugins {
    @Suppress("RemoveRedundantQualifierName")
    val j = io.koosha.foobar.Libraries.Jib

    @Suppress("RemoveRedundantQualifierName")
    val s = io.koosha.foobar.Libraries.Spring

    @Suppress("RemoveRedundantQualifierName")
    val k = io.koosha.foobar.Libraries.Kotlin

    @Suppress("RemoveRedundantQualifierName")
    val d = io.koosha.foobar.Libraries.OpenApi

    id("io.gitlab.arturbosch.detekt") version k.detekt
    id("com.google.cloud.tools.jib") version j.gradlePlugin
    id("org.springdoc.openapi-gradle-plugin") version d.gradlePlugin
    id("org.springframework.boot") version s.springBoot2
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
    implementation(project(":service:common-service"))

    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    // implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-reactor-resilience4j")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
    implementation("org.springframework.cloud:spring-cloud-starter-loadbalancer")
    // implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin")
    // implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    // implementation("org.springframework.boot:spring-boot-starter-security")

    implementation("io.github.microutils:kotlin-logging-jvm:${Libraries.microutilsKotlinLoggingJvm}")
    implementation("net.logstash.logback:logstash-logback-encoder:${Libraries.Log.logstashLogbackEncoder}")


    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springdoc:springdoc-openapi-ui:${Libraries.OpenApi.ui}")
    implementation("org.springdoc:springdoc-openapi-kotlin:${Libraries.OpenApi.kotlin}")
    implementation("org.springdoc:springdoc-openapi-data-rest:${Libraries.OpenApi.rest}")

    runtimeOnly("org.postgresql:postgresql:${Libraries.postgres}")
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:${Libraries.mariadb}")
    runtimeOnly("com.h2database:h2")
    // runtimeOnly("io.r2dbc:r2dbc-h2")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")
    runtimeOnly("io.github.openfeign:feign-micrometer")
    runtimeOnly("io.micrometer:micrometer-registry-jmx")
    runtimeOnly("io.github.openfeign:feign-okhttp")
    runtimeOnly("com.squareup.okhttp3:okhttp")

    // implementation("javax.persistence:javax.persistence-api:2.2")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    // testImplementation("io.projectreactor:reactor-test")
    // testImplementation("org.springframework.security:spring-security-test")
}

openApi {
    apiDocsUrl.set("http://localhost:${Foobar.Port.customer(project)}/${Foobar.OpenApi.httpRelativePaht}")
    outputDir.set(file("$projectDir/${Foobar.OpenApi.projectRelativePath}"))
    outputFileName.set(Foobar.OpenApi.fileName)
    waitTimeInSeconds.set(15)
}

tasks.register<Delete>(Foobar.Gradle.foobarCleanOpenApiTask) {
    this.delete.add(file("$projectDir/${Foobar.OpenApi.projectRelativeFileName}"))
}

tasks.named<Delete>("clean") {
    this.dependsOn(tasks.named(Foobar.Gradle.foobarCleanOpenApiTask))
}

tasks.withType(OpenApiGeneratorTask::class.java) {
    outputs.upToDateWhen { false }
}

tasks.withType<BootRun> {
    jvmArgs = Foobar.Boot.jvmArgs(project)
}

jib {
    extraDirectories.setPaths(
        Foobar.Jib.extraDirs(project)
    )
    container {
        ports = listOf("8080")
        jvmFlags = Foobar.Jib.jvmFlags(project)
    }
    to {
        image = "foobar-customer"
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = Foobar.kotlinJvmTarget
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = Foobar.kotlinJvmTarget
}
