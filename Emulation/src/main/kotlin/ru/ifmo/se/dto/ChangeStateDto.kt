package ru.ifmo.se.dto

data class ChangeStateDto(
    val hubId: Long,
    val update: UpdateDto
)

data class UpdateDto(
    val stateId: Long,
    val switch: SwitchDto?,
    val rangeSwitch: RangeSwitchDto?,
)

data class SwitchDto(
    val id: Long,
    val enabled: Boolean,
)
data class RangeSwitchDto(
    val id: Long,
    val enabled: Boolean,
    val value: Double,
)
data class SensorDto(
    val id: Long,
    val value: Double,
)