package ru.ifmo.se.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking
import ru.ifmo.se.dao.RedisDao
import ru.ifmo.se.dto.ErrorResponse
import ru.ifmo.se.dto.ExpandRoomFrontDto
import ru.ifmo.se.dto.RoomsFrontDto
import ru.ifmo.se.dto.StateDto
import ru.ifmo.se.dto.rooms
import ru.ifmo.se.plugins.UserPrincipal

fun Application.roomRoutes() {
    val redisDao = RedisDao()

    routing {
        authenticate {
            get("/api/rooms") {
                val principal = call.principal<UserPrincipal>()!!
                redisDao.requestState(principal.hubId)
                call.respond(RoomsFrontDto(rooms))
            }
            get("/api/rooms/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val roomId: Long = when (val queryRoomId: String = call.parameters["id"]!!) {
                    "outside" -> 1
                    "home" -> 2
                    else -> queryRoomId.toLong()
                }

                val state: StateDto? = redisDao.getAndInvalidateState(principal.hubId)
                    ?: runBlocking {
                        redisDao.tryGetState(principal.hubId)
                    }

                state
                    ?.let { stateDto ->
                        stateDto.rooms.find { room -> room.id == roomId }
                            ?.let { room ->
                                call.respond(
                                    ExpandRoomFrontDto(
                                        roomId,
                                        room.name,
                                        room.type,
                                        stateDto.stateId,
                                        room.sensors,
                                        room.switches,
                                        room.rangeSwitches
                                    )
                                )
                            }
                            ?: call.respond(HttpStatusCode.NotFound, ErrorResponse("Room not found"))
                    }
                    ?: run {
                        call.respond(HttpStatusCode.GatewayTimeout, ErrorResponse("Hub connection timeout"))
                    }
            }
        }
    }
}
