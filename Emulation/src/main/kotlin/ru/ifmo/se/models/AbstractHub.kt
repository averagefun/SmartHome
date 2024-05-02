package ru.ifmo.se.models

abstract class AbstractHub(
    open val hubId: Long,
    open var stateId: Long? = null,
) {
    abstract fun setState(value: String)
    abstract fun getState(): String
}