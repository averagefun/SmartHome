package ru.ifmo.se.routes

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import ru.ifmo.se.dto.RangeSwitchResponse
import ru.ifmo.se.dto.RoomResponse
import ru.ifmo.se.dto.RoomStateResponse
import ru.ifmo.se.dto.RoomsResponse
import ru.ifmo.se.dto.SensorResponse
import ru.ifmo.se.dto.SwitchResponse
import ru.ifmo.se.plugins.RedisSingleton
import ru.ifmo.se.plugins.UserPrincipal

fun Application.roomRoutes() {
    routing {
        authenticate {
            get("/api/rooms") {
                val principal = call.principal<UserPrincipal>()!!
                RedisSingleton.publish("state.request", principal.hubId.toString())

                val rooms = listOf(
                    RoomResponse(1, "Маленькая гостинная", "living"),
                    RoomResponse(2, "Большая гостинная", "living"),
                    RoomResponse(3, "Кухня", "kitchen")
                )
                call.respond(RoomsResponse(rooms))
            }
            get("/api/rooms/home/state") {
                val principal = call.principal<UserPrincipal>()!!
                RedisSingleton.publish("state.request", principal.hubId.toString())

                val switches = listOf(
                    SwitchResponse(1, "WiFi", "power", true),
                    SwitchResponse(2, "Входная дверь", "lock", false),
                )
                call.respond(RoomStateResponse(1L, emptyList(), switches, emptyList()))
            }
            get("/api/rooms/{id}/state") {
                val principal = call.principal<UserPrincipal>()!!
                RedisSingleton.publish("state.request", principal.hubId.toString())

                val sensors = listOf(
                    SensorResponse(1, "Температура", "temperature", 20.1),
                    SensorResponse(2, "Температура в аквариуме", "temperature", 23.0),
                    SensorResponse(3, "Общий свет", "light", 56.0),
                )

                val switches = listOf(
                    SwitchResponse(1, "Розетка (чайник)", "power", true),
                    SwitchResponse(2, "Розетка (холодильник)", "power", false),
                )

                val rangeSwitches = listOf(
                    RangeSwitchResponse(1, "Большой кондиционер", "climat", true, 21.0),
                    RangeSwitchResponse(2, "Свет над аквариумом", "light", true, 50.0),
                )

                call.respond(RoomStateResponse(1L, sensors, switches, rangeSwitches))
            }
        }
    }
}
