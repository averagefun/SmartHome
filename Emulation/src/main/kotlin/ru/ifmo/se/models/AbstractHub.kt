package ru.ifmo.se.models

import kotlinx.coroutines.CoroutineScope
import ru.ifmo.se.dto.StateDto
import ru.ifmo.se.dto.UpdateDto

abstract class AbstractHub(
    open val hubId: Long,
    open var stateId: Long
) {
    abstract fun setStateWithUpdate(updateDto: UpdateDto)

    abstract fun getState(): StateDto

    abstract fun startSimulation(scope: CoroutineScope, timeRate: Long)
}