package ru.ifmo.se.models

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import ru.ifmo.se.dto.RangeSwitchDto
import ru.ifmo.se.dto.RoomDto
import ru.ifmo.se.dto.SensorDto
import ru.ifmo.se.dto.StateDto
import ru.ifmo.se.dto.SwitchDto
import ru.ifmo.se.dto.UpdateDto
import ru.ifmo.se.dto.UpdateRangeSwitchDto
import ru.ifmo.se.dto.UpdateSwitchDto
import java.util.Locale

open class Hub(
    override val hubId: Long,
    var rooms: MutableList<Room> = mutableListOf()
) : AbstractHub(hubId, 1L) {
    private var updateJob: Job? = null

    override fun setStateWithUpdate(updateDto: UpdateDto) {
        if (updateDto.hubId != hubId || updateDto.update.stateId <= stateId) return

        stateId = updateDto.update.stateId

        val update = updateDto.update
        if (update.switch != null) {
            processSwitchUpdate(update.switch)
        }
        if (update.rangeSwitch != null) {
            processRangeSwitchUpdate(update.rangeSwitch)
        }
    }

    private fun processSwitchUpdate(updateSwitch: UpdateSwitchDto) {
        for (room in rooms) {
            val switch = room.switches.find { it.id == updateSwitch.id }
            if (switch != null) {
                switch.enabled = updateSwitch.enabled
            }
        }
    }

    private fun processRangeSwitchUpdate(updateRangeSwitch: UpdateRangeSwitchDto) {
        for (room in rooms) {
            val rangeSwitch = room.rangeSwitches.find { it.id == updateRangeSwitch.id }
            if (rangeSwitch != null) {
                rangeSwitch.enabled = updateRangeSwitch.enabled
                rangeSwitch.updateValue(updateRangeSwitch.value)
                return
            }
        }
    }

    override fun getState(): StateDto {
        val roomDtos = rooms.map { it.toRoomDto() }
        return StateDto(stateId = hubId, rooms = roomDtos)
    }

    override fun startSimulation(scope: CoroutineScope, timeRate: Long) {
        updateJob = scope.launch(Dispatchers.IO) {
            while (isActive) {
                rooms.forEach{it.update()}
                delay(timeRate)
            }
        }
    }

    fun stopSimulation() {
        updateJob?.cancel()
    }

    private fun Room.toRoomDto() = RoomDto(
        id = id,
        name = name,
        type = type,
        sensors = sensors.map { it.toSensorDto() },
        switches = switches.map { it.toSwitchDto() },
        rangeSwitches = rangeSwitches.map { it.toRangeSwitchDto() }
    )

    private fun Sensor.toSensorDto() = SensorDto(
        id = id,
        name = name,
        type = type.name.lowercase(Locale.getDefault()),
        value = getValue() ?: Double.MIN_VALUE
    )

    private fun Switch.toSwitchDto() = SwitchDto(
        id = id,
        name = name,
        type = type,
        enabled = enabled
    )

    private fun RangeSwitch.toRangeSwitchDto() = RangeSwitchDto(
        id = id,
        name = name,
        type = type,
        enabled = enabled,
        value = value,
        minValue = minValue,
        maxValue = maxValue
    )
}