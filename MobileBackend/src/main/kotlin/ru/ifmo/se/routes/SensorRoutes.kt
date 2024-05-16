package ru.ifmo.se.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import ru.ifmo.se.dto.ErrorResponse
import ru.ifmo.se.dto.SensorHistoryDto
import ru.ifmo.se.dto.SensorHistoryFrontDto
import ru.ifmo.se.logger
import ru.ifmo.se.plugins.ClientService
import ru.ifmo.se.plugins.UserPrincipal
import java.time.LocalDateTime
import java.time.ZoneOffset

fun Application.sensorRoutes() {
    routing {
        authenticate {
            get("/api/sensors/{id}/history") {
                val principal = call.principal<UserPrincipal>()!!
                val sensorId: Long = call.parameters["id"]!!.toLong()

                try {
                    val sensorHistoryDto = ClientService.getSensorHistory(
                        principal.hubId,
                        sensorId,
                        LocalDateTime.now().minusHours(12),
                        LocalDateTime.now()
                    )
                    call.respond(buildSensorHistoryFrontDto(sensorId, sensorHistoryDto))
                } catch (e: Exception) {
                    logger.error("Sensor history exception", e)
                    call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Failed to get sensor history"))
                }
            }
        }
    }
}

fun buildSensorHistoryFrontDto(sensorId: Long, sensorHistoryDto: SensorHistoryDto): SensorHistoryFrontDto {
    val sortedStates = sensorHistoryDto.states
        .sortedBy { it.date }

    val seconds = mutableListOf<Long>()
    val values = mutableListOf<Double>()

    sortedStates.forEach { value ->
        seconds.add(value.date.toEpochSecond(ZoneOffset.UTC))
        values.add(value.value)
    }

    return SensorHistoryFrontDto(sensorId, seconds, values)
}
