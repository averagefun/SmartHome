package ru.ifmo.se.dto

import ru.ifmo.se.model.AggregatedState
import ru.ifmo.se.model.HubState

data class StateLogs(
    val states: List<HubState>
)

data class StateResponse(
    val states: List<AggregatedState>
)
