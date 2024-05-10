package ru.ifmo.se.dto

data class RoomsFrontDto(
    val rooms: List<RoomFrontDto>
)

data class RoomFrontDto(
    val id: Long,
    val name: String,
    val type: String
)

data class ExpandRoomFrontDto(
    val id: Long,
    val name: String,
    val type: String,
    val stateId: Long,
    val sensors: List<SensorDto>,
    val switches: List<SwitchDto>,
    val rangeSwitches: List<RangeSwitchDto>
)

val rooms = listOf(
    RoomFrontDto(0, "Улица", "outside"),
    RoomFrontDto(1, "Дом", "home"),
    RoomFrontDto(3, "Прихожая", "hallway"),
    RoomFrontDto(4, "Гостиная", "living"),
    RoomFrontDto(5, "Спальня", "living"),
    RoomFrontDto(6, "Ванная", "bathroom"),
    RoomFrontDto(7, "Кухня", "kitchen")
)
