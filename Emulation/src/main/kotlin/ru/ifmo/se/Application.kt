package ru.ifmo.se

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.ifmo.se.plugins.*
import ru.ifmo.se.service.HubService

fun main() {
    embeddedServer(Netty, port = 8090, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureRedis()
    HubService.configure()
}
