package ru.ifmo.se.models

import ru.ifmo.se.dto.RangeSwitchDto
import ru.ifmo.se.dto.RoomDto
import ru.ifmo.se.dto.SensorDto
import ru.ifmo.se.dto.StateDto
import ru.ifmo.se.dto.SwitchDto
import ru.ifmo.se.dto.UpdateDto
import kotlin.random.Random

class Hub(
    override val hubId: Long,
    override var state: StateDto = StateDto(
        stateId = 0,
        rooms = listOf(
            RoomDto(
                id = Random.nextLong(1, 1000),
                name = "roomName",
                type = "roomType",
                sensors = listOf(SensorDto(id = Random.nextLong(1, 1000), value = Random.nextDouble(1.0, 1000.0))),
                switches = listOf(SwitchDto(id = Random.nextLong(1, 1000), enabled = true)),
                rangeSwitches = listOf(RangeSwitchDto(id = Random.nextLong(1, 1000), enabled = true, value = Random.nextDouble(1.0, 1000.0)))
            )
        )
    ),
) : AbstractHub(hubId, state) {

    override fun setStateWithUpdate(updateDto: UpdateDto) {
        TODO("Not yet implemented")
    }
}