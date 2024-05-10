package ru.ifmo.se.dto

data class UpdateDto(
    val hubId: Long,
    val update: UpdateStateDto
)

data class UpdateStateDto(
    val stateId: Long,
    val switch: UpdateSwitchDto?,
    val rangeSwitch: UpdateRangeSwitchDto?
)

data class UpdateSwitchDto(
    val id: Long,
    val enabled: Boolean,
)

data class UpdateRangeSwitchDto(
    val id: Long,
    val enabled: Boolean,
    val value: Double,
)
