package ru.ifmo.se.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import ru.ifmo.se.dao.HubDao
import ru.ifmo.se.dto.StateResponse

fun Application.configureRouting() {
    val hubDao = HubDao(environment.config.property("storage.clickhouse.url").getString())

    routing {
        get("/api/state/{hub_id}/{id}") {
            val states = hubDao.getStates(
                call.parameters["hub_id"]!!.toLong(),
                call.parameters["id"]!!.toLong(),
                call.request.queryParameters["from"],
                call.request.queryParameters["to"]
            )

            call.respond(StateResponse(states))
        }
    }
}


