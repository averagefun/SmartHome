package ru.ifmo.se.dto

data class StateRequestDto(
    val hubId: Long
)

data class StateResponseDto(
    var hubId: Long,
    val state: StateDto
)

data class StateDto(
    var stateId: Long,
    val rooms: List<RoomDto>
)

data class RoomDto(
    val id: Long,
    val name: String,
    val type: String,
    val sensors: List<SensorDto> = emptyList(),
    val switches: List<SwitchDto> = emptyList(),
    val rangeSwitches: List<RangeSwitchDto> = emptyList(),
)

data class SensorDto(
    val id: Long,
    val name: String,
    val type: String,
    val value: Double,
)

data class SwitchDto(
    val id: Long,
    val name: String,
    val type: String,
    var enabled: Boolean,
)

data class RangeSwitchDto(
    val id: Long,
    val name: String,
    val type: String,
    var enabled: Boolean,
    var value: Double,
    val minValue: Double,
    val maxValue: Double
)