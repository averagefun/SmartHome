package ru.ifmo.se.dto

class StateDto(
    val hubId: Long,
    val rooms: List<ExpandRoomDto>
)

class RoomsDto(
    val rooms: List<RoomDto>
)

class RoomDto(
    val id: Long,
    val name: String,
    val type: String
)

class ExpandRoomDto(
    val id: Long,
    val name: String,
    val type: String,
    val stateId: Long,
    val sensors: List<SensorDto>,
    val switches: List<SwitchDto>,
    val rangeSwitches: List<RangeSwitchDto>
)

class RoomStateDto(
    val stateId: Long,
    val sensors: List<SensorDto>,
    val switches: List<SwitchDto>,
    val rangeSwitches: List<RangeSwitchDto>
)

class SensorDto(
    val id: Long,
    val name: String,
    val type: String,
    val value: Double
)

class SwitchDto(
    val id: Long,
    val name: String,
    val type: String,
    val enabled: Boolean
)

class RangeSwitchDto(
    val id: Long,
    val name: String,
    val type: String,
    val enabled: Boolean,
    val value: Double
)
