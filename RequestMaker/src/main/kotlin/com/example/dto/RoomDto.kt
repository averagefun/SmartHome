package com.example.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthDto(
    val token: String
)

@Serializable
data class RequestAuthDto(
    val username: String,
    val password: String,
)

@Serializable
data class RoomDto(
    val id: Long,
    val name: String?,
    val type: String?,
    val stateId: Long,
    val sensors: List<SensorDto> = emptyList(),
    val switches: List<SwitchDto> = emptyList(),
    val rangeSwitches: List<RangeSwitchDto> = emptyList(),
)

@Serializable
data class SensorDto(
    val id: Long,
    val name: String?,
    val type: String?,
    val value: Double,
)

@Serializable
data class SwitchDto(
    val id: Long,
    val name: String?,
    val type: String?,
    var enabled: Boolean,
)

@Serializable
data class RangeSwitchDto(
    val id: Long,
    val name: String,
    val type: String,
    var enabled: Boolean,
    var value: Double,
    val minValue: Double,
    val maxValue: Double
)

@Serializable
data class UpdateSwitchDto(
    val stateId: Long,
    val enabled: Boolean,
)

@Serializable
data class UpdateRangeSwitchDto(
    val stateId: Long,
    val enabled: Boolean,
    val value: Double,
)
