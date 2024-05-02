package ru.ifmo.se.models

class Hub(
    override val hubId: Long,
    override var stateId: Long? = null,
) : AbstractHub(hubId, stateId) {

    constructor(id: Long) : this(id, null)
    override fun setState(value: String){}

    override fun getState(): String = "aboba"
}