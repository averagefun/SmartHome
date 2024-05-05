package ru.ifmo.se.routes

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.application.Application

fun Application.configureRoutes() {
    authRoutes()
    roomRoutes()
}
