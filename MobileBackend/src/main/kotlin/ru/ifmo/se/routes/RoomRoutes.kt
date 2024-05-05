package ru.ifmo.se.routes

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.routing
import ru.ifmo.se.dao.RedisDao
import ru.ifmo.se.dto.ExpandRoomDto
import ru.ifmo.se.dto.MessageResponse
import ru.ifmo.se.dto.RangeSwitchDto
import ru.ifmo.se.dto.RoomDto
import ru.ifmo.se.dto.RoomsDto
import ru.ifmo.se.dto.SensorDto
import ru.ifmo.se.dto.SwitchDto
import ru.ifmo.se.plugins.UserPrincipal

fun Application.roomRoutes() {
    val redisDao = RedisDao()
    var stateId = 1L

    routing {
        authenticate {
            get("/api/rooms") {
                val principal = call.principal<UserPrincipal>()!!

                // val state: StateDto? = redisDao.getState(principal.hubId)

                val rooms = listOf(
                    RoomDto(1, "Маленькая гостинная", "living"),
                    RoomDto(2, "Большая гостинная", "living"),
                    RoomDto(3, "Кухня", "kitchen")
                )
                call.respond(RoomsDto(rooms))
            }
            get("/api/rooms/home") {
                val principal = call.principal<UserPrincipal>()!!

                val switches = listOf(
                    SwitchDto(1, "WiFi", "power", true),
                    SwitchDto(2, "Входная дверь", "lock", false),
                )

                return@get call.respond(
                    ExpandRoomDto(0L, "Home", "Home", stateId, emptyList(), switches, emptyList())
                )
            }
            get("/api/rooms/{id}") {
                val roomId: Long = call.parameters["id"]!!.toLong()
                val principal = call.principal<UserPrincipal>()!!
                // RedisSingleton.publish("state.ktor request", principal.hubId.toString())

                if (roomId == 0L) {
                    val switches = listOf(
                        SwitchDto(1, "WiFi", "power", true),
                        SwitchDto(2, "Входная дверь", "lock", false),
                    )

                    return@get call.respond(
                        ExpandRoomDto(roomId, "Home", "Home", stateId, emptyList(), switches, emptyList())
                    )
                }

                val sensors = listOf(
                    SensorDto(1, "Температура", "temperature", 20.1),
                    SensorDto(2, "Температура в аквариуме", "temperature", 23.0),
                    SensorDto(3, "Общий свет", "light", 56.0),
                )

                val switches = listOf(
                    SwitchDto(1, "Розетка (чайник)", "power", true),
                    SwitchDto(2, "Розетка (холодильник)", "power", false),
                )

                val rangeSwitches = listOf(
                    RangeSwitchDto(1, "Большой кондиционер", "climat", true, 21.0),
                    RangeSwitchDto(2, "Свет над аквариумом", "light", true, 50.0),
                )

                call.respond(
                    ExpandRoomDto(roomId, "Name", "Type", stateId, sensors, switches, rangeSwitches)
                )
            }
            patch("/api/switches/{id}") {
                stateId++;
                call.respond(MessageResponse("Scheduled to update"))
            }
            patch("/api/rangeSwitches/{id}") {
                stateId++;
                call.respond(MessageResponse("Scheduled to update"))
            }
        }
    }
}
