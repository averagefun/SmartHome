package ru.ifmo.se.routes

import io.ktor.server.application.Application

fun Application.configureRoutes() {
    authRoutes()
    roomRoutes()
}
