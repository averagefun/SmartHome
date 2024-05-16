package ru.ifmo.se.models

import ru.ifmo.se.models.SensorType.HUMIDITY
import ru.ifmo.se.models.SensorType.POWER
import ru.ifmo.se.models.SensorType.TEMPERATURE

class SmartHomeHub(
    override val hubId: Long,
) : Hub(hubId) {
    init {
        val outside = Outside(
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
            ),

            startTemperature = 18.0,
            temperatureDx = 0.005,
            temperatureCoeff = 0.01
        ).addRoomSensor(
            id = 1L,
            name = "Температура",
            type = TEMPERATURE
        ).addRoomSensor(
            id = 2L,
            name = "Влажность",
            type = HUMIDITY
        ) as Outside

        val home = Home(
            id = 2L,
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
        ).addRoomSensor(
            id = 3L,
            name = "Средняя температура",
            type = TEMPERATURE
        ).addRoomSensor(
            id = 4L,
            name = "Общее энергопотребление",
            type = POWER
        ) as Home

        this.rooms = mutableListOf(
            outside,
            home,
            Room(
                id = 3L,
                name = "Прихожая",
                type = "hallway",
                temperatureCoeff = 0.05,
                switches = mutableListOf(
                    Switch(
                        id = 4L,
                        name = "Освещение в прихожей",
                        type = "light",
                        enabled = false
                    )
                )
            ).addRoomSensor(
                id = 5L,
                name = "Температура в прихожей",
                type = TEMPERATURE
            ),
            Room(
                id = 4L,
                name = "Гостиная",
                type = "living",
                temperatureCoeff = 0.07
            )
                .addRoomSensor(
                    id = 6L,
                    name = "Температура в гостиной",
                    type = TEMPERATURE
                ).addCustomSensor(Sensor(
                    id = 7L,
                    name = "Температура в аквариуме",
                    type = TEMPERATURE,
                    source = Aquarium()
                )).addRangeSwitch(
                    AirConditioner(
                        id = 1L,
                        name = "Кондиционер",
                        enabled = true,
                        value = 23.1,
                        minValue = 15.0,
                        maxValue = 28.0
                    )
                ),
            Room(
                id = 5L,
                name = "Спальня",
                type = "living",
                temperatureCoeff = 0.05,
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
            ).addRoomSensor(
                id = 8L,
                name = "Температура в спальне",
                type = TEMPERATURE
            ),
            Room(
                id = 6L,
                name = "Ванная",
                type = "bathroom",
                temperatureCoeff = 0.05,
            ).addRoomSensor(
                id = 9L,
                name = "Температура в ванной",
                type = TEMPERATURE
            ).addCustomSensor(Sensor(
                id = 10L,
                name = "Влажность в ванной",
                type = HUMIDITY,
                source = SensorSource {
                    if (it == HUMIDITY) {
                        val num = (790..799).random().toDouble()/10
                        return@SensorSource num
                    }
                    return@SensorSource null
                }
            )),
            Room(
                id = 7L,
                name = "Кухня",
                type = "kitchen",
                temperatureCoeff = 0.05,
                switches = mutableListOf(
                    Switch(
                        id = 5L,
                        name = "Розетка (чайник)",
                        type = "power",
                        enabled = false,
                        energyDx = 20.0
                    ),
                    Switch(
                        id = 6L,
                        name = "Розетка (холодильник)",
                        type = "power",
                        enabled = true,
                        energyDx = 100.0
                    )
                )
            ),
        )

        val realRooms = rooms.filter { it != outside && it != home}
        realRooms.forEach { home.aggregateTargets.add(it) }

        realRooms.forEach { it.addOutside(outside = outside) }
        realRooms.forEach { room ->
            realRooms.filter { it != room }.forEach { anotherRoom ->
                room.addSensorModifier(anotherRoom)
            }
        }
    }
}