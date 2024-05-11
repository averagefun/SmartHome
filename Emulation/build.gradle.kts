@file:Suppress("PropertyName")

val kotlin_version: String by project
val ktor_version: String by project
val logback_version: String by project
val exposed_version: String by project
val postgresql_version: String by project

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

    // redis
    implementation("io.lettuce:lettuce-core:6.2.6.RELEASE")
    implementation("com.redis:lettucemod:3.6.3")

    implementation("org.slf4j:slf4j-api:1.6.1")
    implementation("org.slf4j:slf4j-simple:1.6.1")
}

ktor {
    fatJar {
        archiveFileName.set("EmulationBackendFat.jar")
    }
}
