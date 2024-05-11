package ru.ifmo.se.models

import ru.ifmo.se.models.SensorType.HUMIDITY
import ru.ifmo.se.models.SensorType.POWER
import ru.ifmo.se.models.SensorType.TEMPERATURE

class SmartHomeHub(
    override val hubId: Long,
) : Hub(hubId, rooms) {}

val rooms = mutableListOf(
    Room(
        id = 1L,
        name = "Улица",
        type = "outside",
        switches = mutableListOf(
            Switch(
                id = 1L,
                name = "Уличное освещение",
                type = "light",
                enabled = false
            )
        )
    ).addSensor(
        id = 1L,
        name = "Температура",
        type = TEMPERATURE
    ).addSensor(
        id = 2L,
        name = "Влажность",
        type = HUMIDITY
    ),
    Room(
        id = 2L,
        name = "Дом",
        type = "home",
        switches = mutableListOf(
            Switch(
                id = 2L,
                name = "WiFi",
                type = "power",
                enabled = false
            ),
            Switch(
                id = 3L,
                name = "Входная дверь",
                type = "lock",
                enabled = true
            )
        )
    ).addSensor(
        id = 3L,
        name = "Средняя температура",
        type = TEMPERATURE
    ).addSensor(
        id = 4L,
        name = "Общее энергопотребление",
        type = POWER
    ),
    Room(
        id = 3L,
        name = "Прихожая",
        type = "hallway",
        switches = mutableListOf(
            Switch(
                id = 4L,
                name = "Освещение в прихожей",
                type = "light",
                enabled = false
            )
        )
    ).addSensor(
        id = 5L,
        name = "Температура в прихожей",
        type = TEMPERATURE
    ),
    Room(
        id = 4L,
        name = "Гостиная",
        type = "living",
        rangeSwitches = mutableListOf(
            RangeSwitch(
                id = 1L,
                name = "Кондиционер",
                type = "climat",
                enabled = false,
                value = 23.1,
                minValue = 15.0,
                maxValue = 28.0
            )
        )
    )
        .addSensor(
            id = 6L,
            name = "Температура в гостиной",
            type = TEMPERATURE
        ).addSensor(
            id = 7L,
            name = "Температура в аквариуме",
            type = TEMPERATURE
        ),
    Room(
        id = 5L,
        name = "Спальня",
        type = "living",
        rangeSwitches = mutableListOf(
            RangeSwitch(
                id = 2L,
                name = "Освещение",
                type = "light",
                enabled = true,
                value = 40.0,
                minValue = 0.0,
                maxValue = 100.0
            )
        )
    ).addSensor(
        id = 8L,
        name = "Температура в спальне",
        type = TEMPERATURE
    ),
    Room(
        id = 6L,
        name = "Ванная",
        type = "bathroom",
    ).addSensor(
        id = 9L,
        name = "Температура в ванной",
        type = TEMPERATURE
    ).addSensor(
        id = 10L,
        name = "Влажность в ванной",
        type = HUMIDITY
    ),
    Room(
        id = 7L,
        name = "Кухня",
        type = "kitchen",
        switches = mutableListOf(
            Switch(
                id = 5L,
                name = "Розетка (чайник)",
                type = "power",
                enabled = false
            ),
            Switch(
                id = 6L,
                name = "Розетка (холодильник)",
                type = "power",
                enabled = true
            )
        )
    ),
)