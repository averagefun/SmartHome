package ru.ifmo.se

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import org.slf4j.LoggerFactory
import ru.ifmo.se.plugins.configureRedis
import ru.ifmo.se.plugins.configureRouting

fun main(args: Array<String>): Unit = EngineMain.main(args)

val logger: ch.qos.logback.classic.Logger =
    LoggerFactory.getLogger("db-backend-ktor") as ch.qos.logback.classic.Logger

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureRedis()
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            enable(SerializationFeature.INDENT_OUTPUT)
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            setSerializationInclusion(JsonInclude.Include.NON_NULL)
            configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
        }
    }
}

val objectMapper = jacksonObjectMapper();
