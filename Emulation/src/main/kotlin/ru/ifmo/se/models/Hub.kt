package ru.ifmo.se.models

import ru.ifmo.se.dto.RangeSwitchDto
import ru.ifmo.se.dto.RoomDto
import ru.ifmo.se.dto.SensorDto
import ru.ifmo.se.dto.StateDto
import ru.ifmo.se.dto.SwitchDto
import ru.ifmo.se.dto.UpdateDto

class Hub(
    override val hubId: Long,
    override var state: StateDto = mockState
) : AbstractHub(hubId, state) {

    override fun setStateWithUpdate(updateDto: UpdateDto) {
        TODO("Not yet implemented")
    }
}

val mockState = StateDto(
    stateId = 1L,
    rooms = listOf(
        RoomDto(
            id = 1L,
            name = "Улица",
            type = "outside",
            sensors = listOf(
                SensorDto(
                    id = 1L,
                    name = "Температура",
                    type = "temperature",
                    value = 25.0
                ),
                SensorDto(
                    id = 2L,
                    name = "Влажность",
                    type = "humidity",
                    value = 45.0
                )
            ),
            switches = listOf(
                SwitchDto(
                    id = 1L,
                    name = "Уличное освещение",
                    type = "light",
                    enabled = false
                )
            )
        ),
        RoomDto(
            id = 2L,
            name = "Дом",
            type = "home",
            sensors = listOf(
                SensorDto(
                    id = 3L,
                    name = "Средняя температура",
                    type = "temperature",
                    value = 24.0
                ),
                SensorDto(
                    id = 4L,
                    name = "Общее энергопотребление",
                    type = "power",
                    value = 70.0
                )
            ),
            switches = listOf(
                SwitchDto(
                    id = 2L,
                    name = "WiFi",
                    type = "power",
                    enabled = false
                ),
                SwitchDto(
                    id = 3L,
                    name = "Входная дверь",
                    type = "lock",
                    enabled = true
                )
            )
        ),
        RoomDto(
            id = 3L,
            name = "Прихожая",
            type = "hallway",
            sensors = listOf(
                SensorDto(
                    id = 5L,
                    name = "Температура в прихожей",
                    type = "temperature",
                    value = 23.0
                )
            ),
            switches = listOf(
                SwitchDto(
                    id = 4L,
                    name = "Освещение в прихожей",
                    type = "light",
                    enabled = false
                )
            )
        ),
        RoomDto(
            id = 4L,
            name = "Гостиная",
            type = "living",
            sensors = listOf(
                SensorDto(
                    id = 6L,
                    name = "Температура в гостиной",
                    type = "temperature",
                    value = 22.3
                ),
                SensorDto(
                    id = 7L,
                    name = "Температура в аквариуме",
                    type = "temperature",
                    value = 26.1
                )
            ),
            rangeSwitches = listOf(
                RangeSwitchDto(
                    id = 1L,
                    name = "Кондиционер",
                    type = "climat",
                    enabled = false,
                    value = 23.1,
                    minValue = 15.0,
                    maxValue = 28.0
                )
            )
        ),
        RoomDto(
            id = 5L,
            name = "Спальня",
            type = "living",
            sensors = listOf(
                SensorDto(
                    id = 8L,
                    name = "Температура в спальне",
                    type = "temperature",
                    value = 22.3
                )
            ),
            rangeSwitches = listOf(
                RangeSwitchDto(
                    id = 2L,
                    name = "Освещение",
                    type = "light",
                    enabled = true,
                    value = 40.0,
                    minValue = 0.0,
                    maxValue = 100.0
                )
            )
        ),
        RoomDto(
            id = 6L,
            name = "Ванная",
            type = "bathroom",
            sensors = listOf(
                SensorDto(
                    id = 9L,
                    name = "Температура в ванной",
                    type = "temperature",
                    value = 25.1
                ),
                SensorDto(
                    id = 10L,
                    name = "Влажность в ванной",
                    type = "humidity",
                    value = 68.0
                )
            ),
        ),
        RoomDto(
            id = 7L,
            name = "Кухня",
            type = "kitchen",
            switches = listOf(
                SwitchDto(
                    id = 5L,
                    name = "Розетка (чайник)",
                    type = "power",
                    enabled = false
                ),
                SwitchDto(
                    id = 6L,
                    name = "Розетка (холодильник)",
                    type = "power",
                    enabled = true
                )
            )
        ),
    )
)
