package ru.ifmo.se.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.patch
import io.ktor.server.routing.routing
import ru.ifmo.se.dao.RedisDao
import ru.ifmo.se.dto.UpdateDto
import ru.ifmo.se.dto.UpdateRangeSwitchDto
import ru.ifmo.se.dto.UpdateRangeSwitchFrontDto
import ru.ifmo.se.dto.UpdateStateDto
import ru.ifmo.se.dto.UpdateSwitchDto
import ru.ifmo.se.dto.UpdateSwitchFrontDto
import ru.ifmo.se.plugins.UserPrincipal

fun Application.switchRoutes() {
    val redisDao = RedisDao()

    routing {
        authenticate {
            patch("/api/switches/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val switchId: Long = call.parameters["id"]!!.toLong()
                val updateSwitchFrontDto = call.receive<UpdateSwitchFrontDto>()

                redisDao.requestUpdate(
                    UpdateDto(
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

                redisDao.requestUpdate(
                    UpdateDto(
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
