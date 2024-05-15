package com.example.dto

data class AuthDto(
    val token: String
)

data class RoomDto(
    val id: Long,
    val name: String?,
    val type: String?,
    val stateId: Long,
    val sensors: List<SensorDto> = emptyList(),
    val switches: List<SwitchDto> = emptyList(),
    val rangeSwitches: List<RangeSwitchDto> = emptyList(),
)
data class SensorDto(
    val id: Long,
    val name: String?,
    val type: String?,
    val value: Double,
)

data class SwitchDto(
    val id: Long,
    val name: String?,
    val type: String?,
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

data class UpdateSwitchDto(
    val stateId: Long,
    val enabled: Boolean,
)

data class UpdateRangeSwitchDto(
    val stateId: Long,
    val enabled: Boolean,
    val value: Double,
)
