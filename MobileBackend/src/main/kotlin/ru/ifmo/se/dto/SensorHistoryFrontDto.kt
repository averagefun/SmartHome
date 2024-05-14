package ru.ifmo.se.dto

data class SensorHistoryFrontDto(
    val id: Long,
    val seconds: List<Long>,
    val values: List<Double>
)
