package ru.ifmo.se.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.routing
import kotlinx.coroutines.runBlocking
import ru.ifmo.se.dao.RedisDao
import ru.ifmo.se.dto.ErrorResponse
import ru.ifmo.se.dto.ExpandRoomFrontDto
import ru.ifmo.se.dto.RoomDto
import ru.ifmo.se.dto.RoomsFrontDto
import ru.ifmo.se.dto.StateDto
import ru.ifmo.se.dto.UpdateDto
import ru.ifmo.se.dto.UpdateRangeSwitchDto
import ru.ifmo.se.dto.UpdateRangeSwitchFrontDto
import ru.ifmo.se.dto.UpdateStateDto
import ru.ifmo.se.dto.UpdateSwitchDto
import ru.ifmo.se.dto.UpdateSwitchFrontDto
import ru.ifmo.se.dto.rooms
import ru.ifmo.se.plugins.UserPrincipal

fun Application.roomRoutes() {
    val redisDao = RedisDao()

    routing {
        authenticate {
            get("/api/rooms") {
                call.respond(RoomsFrontDto(rooms))
            }
            get("/api/rooms/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val roomId: Long = when (val queryRoomId: String = call.parameters["id"]!!) {
                    "outside" -> 0
                    "home" -> 1
                    else -> queryRoomId.toLong()
                }

                val state: StateDto? = redisDao.getAndInvalidateState(principal.hubId)
                    ?: runBlocking {
                        redisDao.tryGetState(principal.hubId)
                    }

                state
                    ?.let { stateDto ->
                        val room: RoomDto = stateDto.rooms.find { room ->
                            room.id == roomId
                        }!!
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
                    ?: run {
                        call.respond(HttpStatusCode.GatewayTimeout, ErrorResponse("Hub connection timeout"))
                    }
            }
            patch("/api/switches/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val switchId: Long = call.parameters["id"]!!.toLong()
                val updateSwitchFrontDto = call.receive<UpdateSwitchFrontDto>()

                redisDao.requestUpdate(UpdateDto(
                        principal.hubId, UpdateStateDto(
                            updateSwitchFrontDto.stateId,
                            switch = UpdateSwitchDto(switchId, updateSwitchFrontDto.enabled),
                            rangeSwitch = null
                        )
                    )
                )
                call.respond(HttpStatusCode.Accepted, "Switch update accepted")
            }
            patch("/api/rangeSwitches/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val rangeSwitchId: Long = call.parameters["id"]!!.toLong()
                val updateRangeSwitchFrontDto = call.receive<UpdateRangeSwitchFrontDto>()

                redisDao.requestUpdate(UpdateDto(
                        principal.hubId, UpdateStateDto(
                            updateRangeSwitchFrontDto.stateId,
                            switch = null,
                            rangeSwitch = UpdateRangeSwitchDto(
                                rangeSwitchId,
                                updateRangeSwitchFrontDto.enabled,
                                updateRangeSwitchFrontDto.value
                            )
                        )
                    )
                )
                call.respond(HttpStatusCode.Accepted, "RangeSwitch update accepted")
            }
        }
    }
}
