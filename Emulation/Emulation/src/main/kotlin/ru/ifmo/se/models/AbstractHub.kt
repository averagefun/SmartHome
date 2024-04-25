package ru.ifmo.se.models

abstract class AbstractHub(
    val hubId: Long,
    var stateId: Long? = null,
) {
    abstract fun setState(value: String)
    abstract fun getState(): String
}