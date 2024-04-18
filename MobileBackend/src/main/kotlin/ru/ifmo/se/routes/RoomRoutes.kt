package ru.ifmo.se.routes

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import ru.ifmo.se.dto.RangeSwitchResponse
import ru.ifmo.se.dto.RoomResponse
import ru.ifmo.se.dto.RoomStateResponse
import ru.ifmo.se.dto.RoomsResponse
import ru.ifmo.se.dto.SensorResponse
import ru.ifmo.se.dto.SwitchResponse

fun Application.roomRoutes() {
    routing {
        authenticate {
            get("/api/rooms") {
                val rooms = listOf(
                    RoomResponse(1, "Маленькая гостинная", "living"),
                    RoomResponse(2, "Большая гостинная", "living"),
                    RoomResponse(3, "Кухня", "kitchen"),
                    RoomResponse(4, "Дом", "home")
                )
                call.respond(RoomsResponse(rooms));
            }
            get("/api/rooms/home/state") {
                val switches = listOf(
                    SwitchResponse(1, "WiFi", "power", true),
                    SwitchResponse(2, "Входная дверь", "lock", false),
                )
                call.respond(RoomStateResponse(emptyList(), switches, emptyList()))
            }
            get("/api/rooms/{id}/state") {
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

                call.respond(RoomStateResponse(sensors, switches, rangeSwitches))
            }
        }
    }
}
