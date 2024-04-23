package ru.ifmo.se

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import org.slf4j.LoggerFactory
import ru.ifmo.se.plugins.configureDatabase
import ru.ifmo.se.plugins.configureRedis
import ru.ifmo.se.plugins.configureSecurity
import ru.ifmo.se.plugins.configureSerialization
import ru.ifmo.se.routes.configureRoutes

fun main(args: Array<String>): Unit = EngineMain.main(args)

val logger: ch.qos.logback.classic.Logger =
    LoggerFactory.getLogger("mobile-backend-ktor") as ch.qos.logback.classic.Logger

fun Application.module() {
    configureDatabase()
    configureRedis()
    configureSecurity()
    configureSerialization()
    configureRoutes()
}
