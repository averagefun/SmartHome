package ru.ifmo.se.models

import ru.ifmo.se.models.SensorType.*
import java.util.concurrent.ConcurrentHashMap

enum class SensorType {
    TEMPERATURE,
    HUMIDITY,
    POWER
}

interface SensorSource {
    fun getSensorValue(type: SensorType): Double?
}

class Room(
    val id: Long,
    val name: String,
    val type: String,
    val sensors: MutableList<Sensor> = mutableListOf(),
    val switches: MutableList<Switch> = mutableListOf(),
    val rangeSwitches: MutableList<RangeSwitch> = mutableListOf(),

    val sensorSources: ConcurrentHashMap<SensorType, Double> = ConcurrentHashMap()
) : SensorSource {

    override fun getSensorValue(type: SensorType): Double? {
        return sensorSources[type]
    }

    fun update() {
        sensorSources[TEMPERATURE] = (20..30).random().toDouble()
        sensorSources[HUMIDITY] = (70..80).random().toDouble()
        sensorSources[POWER] = (1000..4000).random().toDouble()
    }

    fun addSensor(id: Long, name: String, type: SensorType): Room {
        sensors.add(Sensor(id, name, type, this))
        return this
    }
}

open class Sensor(
    val id: Long,
    val name: String,
    val type: SensorType,
    val source: SensorSource
) {
    open fun getValue(): Double? {
        return source.getSensorValue(type)
    }
}

class Switch(
    val id: Long,
    val name: String,
    val type: String,
    var enabled: Boolean
)

class RangeSwitch(
    val id: Long,
    val name: String,
    val type: String,
    var enabled: Boolean,
    var value: Double,
    val minValue: Double,
    val maxValue: Double
) {
    fun updateValue(newValue: Double) {
        if(newValue in minValue..maxValue) {
            value = newValue
        }
    }
}