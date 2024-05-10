package ru.ifmo.se.dto

data class UpdateSwitchFrontDto(
    val stateId: Long,
    val enabled: Boolean
)

data class UpdateRangeSwitchFrontDto(
    val stateId: Long,
    val enabled: Boolean,
    val value: Double
)
