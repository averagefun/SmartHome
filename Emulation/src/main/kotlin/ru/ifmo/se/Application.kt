package ru.ifmo.se

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.ifmo.se.plugins.*
import ru.ifmo.se.service.HubService

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureRedis()
    HubService.configure(environment.config)
}
