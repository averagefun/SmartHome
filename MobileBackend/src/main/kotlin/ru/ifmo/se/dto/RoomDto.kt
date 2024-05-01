package ru.ifmo.se.dto

class RoomsResponse(
    val rooms: List<RoomResponse>
)

class RoomResponse(
    val id: Long,
    val name: String,
    val type: String
)

class RoomStateResponse(
    val stateId: Long,
    val sensors: List<SensorResponse>,
    val switches: List<SwitchResponse>,
    val rangeSwitches: List<RangeSwitchResponse>
)

class SensorResponse(
    val id: Long,
    val name: String,
    val type: String,
    val value: Double
)

class SwitchResponse(
    val id: Long,
    val name: String,
    val type: String,
    val enabled: Boolean
)

class RangeSwitchResponse(
    val id: Long,
    val name: String,
    val type: String,
    val enabled: Boolean,
    val value: Double
)
