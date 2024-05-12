package ru.ifmo.se.plugins

import io.ktor.http.HttpStatusCode
import ru.ifmo.se.dao.LogDao
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import ru.ifmo.se.model.Log

fun Application.configureRouting() {
    val logDao = LogDao(environment.config.property("storage.url").getString())

    routing {
        post("/log") {
            logDao.saveLogEntity(call.receive<Log>())
            call.respond(HttpStatusCode.Created)
        }
    }
}
