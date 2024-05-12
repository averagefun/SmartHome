package ru.ifmo.se.model

import java.time.LocalDateTime

data class AggregatedState (
    val hubId: Long,
    val id: Long,
    val value: Double,
    val date: LocalDateTime
)