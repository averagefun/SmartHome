package ru.ifmo.se.dto

data class StateResponseDto(
    val hubId: Long,
    val state: StateDto,
)

data class StateDto(
    var stateId: Long,
    val rooms: List<RoomDto>
)

data class RoomDto(
    val id: Long,
    val name: String,
    val type: String,
    val sensors: List<SensorDto>,
    val switches: List<SwitchDto>,
    val rangeSwitches: List<RangeSwitchDto>,
)
