package ru.ifmo.se.models

import ru.ifmo.se.models.SensorType.HUMIDITY
import ru.ifmo.se.models.SensorType.POWER
import ru.ifmo.se.models.SensorType.TEMPERATURE
import java.lang.Math.exp
import java.util.concurrent.ConcurrentHashMap

const val SPEED = 0.03 // 0.30

enum class SensorType {
    TEMPERATURE, HUMIDITY, POWER
}

fun interface SensorSource {
    fun getSensorValue(type: SensorType): Double?
}

fun interface SensorValueModifier {
    fun getSensorValueDx(type: SensorType, prev: Double): Double
}

fun interface SimpleSensorValueDxSource {
    fun getValueDx(prev: Double): Double
}

open class Room(
    val id: Long,
    val name: String,
    val type: String,
    val temperatureCoeff: Double,
    val sensors: MutableList<Sensor> = mutableListOf(),
    open val switches: MutableList<Switch> = mutableListOf(),
    val rangeSwitches: MutableList<RangeSwitch> = mutableListOf(),
    val sensorSources: ConcurrentHashMap<SensorType, Double> = ConcurrentHashMap(),
    val sensorModifiers: MutableList<SensorValueModifier> = mutableListOf()
) : SensorSource,
    SensorValueModifier {

    override fun getSensorValue(type: SensorType): Double? {
        return sensorSources[type]
    }

    open fun update() {
        update(TEMPERATURE, 25.0, 0.0)

        sensorSources[HUMIDITY] = (747..753).random().toDouble()/10
        update(POWER, 0.0, 5.0)
    }

    fun update(type: SensorType, def: Double, defDx: Double) {
        val prev = sensorSources.getOrDefault(type, def)
        sensorSources[type] = prev + getFinalSensorSourceDx(type, prev) + defDx

        println("Update $type in room $name to " + sensorSources[type])
    }

    fun getFinalSensorSourceDx(type: SensorType, prev: Double): Double {
        return sensorModifiers.fold(0.0) { acc, sensorValueModifier ->
            acc + sensorValueModifier.getSensorValueDx(type, prev)
        }
    }

    fun addRoomSensor(id: Long, name: String, type: SensorType): Room {
        sensors.add(Sensor(id, name, type, this))
        return this
    }

    fun addCustomSensor(sensor: Sensor): Room {
        sensors.add(sensor)
        return this
    }

    fun addSwitch(switch: Switch): Room {
        switches.add(switch)
        return addSensorModifier(switch)
    }

    fun addRangeSwitch(switch: RangeSwitch): Room {
        rangeSwitches.add(switch)
        return addSensorModifier(switch)
    }

    fun addSensorModifier(modifier: SensorValueModifier): Room {
        sensorModifiers.add(modifier)
        return this;
    }

    fun addOutside(outside: Outside) {
        sensorSources[TEMPERATURE] = outside.startTemperature
    }

    override fun getSensorValueDx(type: SensorType, prev: Double): Double {
        if(type == TEMPERATURE) {
            val difference = getSensorValue(TEMPERATURE)?.minus(prev)
            val dx = SPEED * temperatureCoeff * difference!! * exp(-SPEED * kotlin.math.abs(difference))
            return dx
        }
        return 0.0;
    }
}

open class Outside(
    id: Long,
    name: String,
    type: String,

    override var switches: MutableList<Switch>,

    val startTemperature: Double,
    val temperatureDx: Double,

    temperatureCoeff: Double
): Room(id, name, type, temperatureCoeff)
{
    init {
        sensorSources[TEMPERATURE] = startTemperature

        sensorModifiers.add(SensorValueModifier { sensorType, prev ->
            if(sensorType == TEMPERATURE) {
                return@SensorValueModifier temperatureDx
            }
            return@SensorValueModifier 0.0
        })
    }
}

open class Home(
    id: Long,
    override var switches: MutableList<Switch>,
    var aggregateTargets: MutableList<SensorSource> = mutableListOf()
): Room(id, "Дом", "home", 0.0) {

    override fun update() {
        sensors.forEach{sensor ->
            val type = sensor.type
            val targets = aggregateTargets.map { it.getSensorValue(type) }
                .filterNotNull()
            var newVal = targets.sum()
            if(type != POWER) {
                newVal /= targets.size
            }
            sensorSources[type] = newVal
            println("Update $type in room $name to " + sensorSources[type])
        }
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

open class Switch(
    open val id: Long,
    open val name: String,
    open val type: String,
    open var enabled: Boolean,

    open val energyDx: Double = 20.0,

    val sensorValueDxSources: MutableMap<SensorType, SimpleSensorValueDxSource> = mutableMapOf()
) : SensorValueModifier {
    init {
        sensorValueDxSources.put(POWER, SimpleSensorValueDxSource {
            return@SimpleSensorValueDxSource if (enabled) energyDx else 0.0
        })
    }
    override fun getSensorValueDx(type: SensorType, prev: Double): Double {
        return sensorValueDxSources[type].let { it ->
            if (it == null || !enabled) {
                return 0.0
            } else {
                return it.getValueDx(prev)
            }
        }
    }
}

open class RangeSwitch(
    override val id: Long,
    override val name: String,
    override val type: String,
    override var enabled: Boolean,
    open var value: Double,
    open val minValue: Double,
    open val maxValue: Double,
) : Switch(id, name, type, enabled), SensorValueModifier {

    init {
        sensorValueDxSources.put(POWER, SimpleSensorValueDxSource {
            val perc = (value - minValue) / (maxValue - minValue)
            return@SimpleSensorValueDxSource if (enabled) energyDx * perc else 0.0
        })
    }

    fun updateValue(newValue: Double) {
        if (newValue in minValue..maxValue) {
            value = newValue
        }
    }
}

class AirConditioner(
    override val id: Long,
    override val name: String,
    override var enabled: Boolean,
    override var value: Double,
    override val minValue: Double,
    override val maxValue: Double,
) : RangeSwitch(id, name, "climat", enabled, value, minValue, maxValue) {
    init {
        sensorValueDxSources.put(TEMPERATURE, SimpleSensorValueDxSource { prev ->
            val difference = value - prev
            val dx =  SPEED * difference * exp(-SPEED * kotlin.math.abs(difference))
            return@SimpleSensorValueDxSource dx
        })
    }
}

class Aquarium(

) : SensorSource {
    override fun getSensorValue(type: SensorType): Double? {
        if(type == TEMPERATURE) {
            return (2500.. 2550).random().toDouble()/100
        }
        return null
    }
}