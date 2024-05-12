package ru.ifmo.se.dto

import ru.ifmo.se.model.AggregatedState

data class StateResponse (
    val states: List<AggregatedState>
)
