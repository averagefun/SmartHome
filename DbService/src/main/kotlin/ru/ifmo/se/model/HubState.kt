package ru.ifmo.se.model

data class HubState (
    val hubId: Long,
    val state: State,
)

data class State (
    val stateId: Long,
    val rooms: List<Room>,
)

data class Room (
    val id: Long,
    val name: String,
    val type: String,
    val sensors: List<Sensor>,
    val switches: List<Switch>,
    val rangeSwitches: List<RangeSwitch>,
)

data class Sensor (
    val id: Long,
    val value: Double,
)

data class Switch (
    val id: Long,
    val enabled: Boolean,
)

data class RangeSwitch (
    val id: Long,
    val enabled: Boolean,
    val value: Double,
)