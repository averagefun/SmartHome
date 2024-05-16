package ru.ifmo.se.model

import java.time.LocalDateTime

data class Log(
    val timestamp: LocalDateTime,
    val thread: String,
    val level: String,
    val logger: String,
    val message: String,
    val environment: String,
    val service: String
)