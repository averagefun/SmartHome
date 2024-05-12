@file:Suppress("PropertyName")

val kotlin_version: String by project
val ktor_version: String by project
val logback_version: String by project
val exposed_version: String by project
val clickhouse_version: String by project

plugins {
    application
    kotlin("jvm") version "1.9.23"
    id("io.ktor.plugin") version "2.3.10"
}

group = "ru.ifmo.se"
version = "1.0"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

dependencies {
    // ktor
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-jackson:$ktor_version")

    // logback
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // database
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("com.clickhouse:clickhouse-jdbc:$clickhouse_version")

    // redis
    implementation("io.lettuce:lettuce-core:6.2.6.RELEASE")
    implementation("com.redis:lettucemod:3.6.3")

    implementation("org.slf4j:slf4j-api:2.0.7")
    implementation("org.lz4:lz4-java:1.8.0")
    implementation("org.redisson:redisson:3.21.3")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
    implementation("com.clickhouse:clickhouse-http-client:0.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("com.github.vitalyros:redisson-kotlin-coroutines-reactive:0.0.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.0")
    implementation("org.json:json:20230618")
    implementation("commons-io:commons-io:2.13.0")
}

ktor {
    fatJar {
        archiveFileName.set("DbServiceFat.jar")
    }
}