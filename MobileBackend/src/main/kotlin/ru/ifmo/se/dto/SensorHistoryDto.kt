package ru.ifmo.se.dto

import java.time.LocalDateTime

data class SensorHistoryDto(
    val states: List<SensorValueDto>
)

data class SensorValueDto(
    val hubId: Long,
    val id: Long,
    val value: Double,
    val date: LocalDateTime
)
