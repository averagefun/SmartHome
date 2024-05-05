package ru.ifmo.se.models

import ru.ifmo.se.dto.StateDto
import ru.ifmo.se.dto.UpdateDto

abstract class AbstractHub(
    open val hubId: Long,
    open val state: StateDto,
) {
    abstract fun setStateWithUpdate(updateDto: UpdateDto)

    fun getStateId() = state.stateId
}