package ru.ifmo.se

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import ru.ifmo.se.dao.DatabaseSingleton
import ru.ifmo.se.plugins.configureSecurity
import ru.ifmo.se.plugins.configureSerialization
import ru.ifmo.se.routes.authRoutes
import ru.ifmo.se.routes.roomRoutes

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    DatabaseSingleton.init(environment.config)
    configureSecurity()
    configureSerialization()

    authRoutes()
    roomRoutes()
}
