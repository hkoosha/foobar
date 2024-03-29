@file:Suppress("RemoveRedundantQualifierName")

import io.koosha.foobar.Foobar
import io.koosha.foobar.Libraries

plugins {
    val p = io.koosha.foobar.Libraries.Proto

    java
    id("com.google.protobuf") version p.protoPlugin apply true
}

java {
    group = Foobar.group
    version = Foobar.appVersion
    sourceCompatibility = JavaVersion.valueOf(Foobar.javaVersion)
    targetCompatibility = JavaVersion.valueOf(Foobar.javaVersion)
}

repositories {
    Foobar.addRepositories(this)
}

dependencies {
    compileOnly("org.apache.kafka:kafka-clients:${Libraries.Kafka.kafka}")

    compileOnly("javax.annotation:javax.annotation-api:${Libraries.javaxAnnotation}")
    implementation("com.google.protobuf:protobuf-java:${Libraries.Proto.proto}")
}

sourceSets {
    main {
        java {
            srcDir("$projectDir/src/generated/main/java")
        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${Libraries.Proto.proto}"
    }

    // generatedFilesBaseDir = "$projectDir/src/generated"
}

// tasks.getByName<Delete>("clean") {
//     delete.add(protobuf.generatedFilesBaseDir)
// }

