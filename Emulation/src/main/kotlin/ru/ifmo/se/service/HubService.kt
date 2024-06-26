package ru.ifmo.se.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.application.*
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ifmo.se.dto.StateRequestDto
import ru.ifmo.se.dto.StateResponseDto
import ru.ifmo.se.dto.UpdateDto
import ru.ifmo.se.models.AbstractHub
import ru.ifmo.se.models.SmartHomeHub
import ru.ifmo.se.plugins.RedisSingleton
import java.util.concurrent.ConcurrentHashMap

class HubService {

    companion object {
        var clientsCount: Int = 0
        val current = HubService()

        @OptIn(DelicateCoroutinesApi::class)
        fun configure(config: ApplicationConfig) {
            clientsCount = config.property("emulation.clientsCount").getString().toInt()
            GlobalScope.launch {
                while (true) {
                    current.publisher.publish(
                        "state.logs",
                        jacksonObjectMapper().writeValueAsString(current.getState())
                    )
                    delay(60000)
                }
            }
            (1L..clientsCount).forEach {
                current.hubs[it] = SmartHomeHub(it)
            }
            current.startSumulation()
        }
    }

    private val hubs: ConcurrentHashMap<Long, AbstractHub> = ConcurrentHashMap()

    @OptIn(DelicateCoroutinesApi::class)
    fun startSumulation() {
        hubs.forEach {
            it.value.startSimulation(GlobalScope, 10_000)
        }
    }

    val publisher = RedisSingleton

    fun getMessage(channel: String, payload: String) {
        println("message '$payload' in chanel '$channel'")
        when (channel) {
            "state.request" -> {
                val body = jacksonObjectMapper().readValue(payload, StateRequestDto::class.java)
                getState(body.hubId)?.let {
                    println(jacksonObjectMapper().writeValueAsString(it))
                    publisher.publish("state.response", jacksonObjectMapper().writeValueAsString(it))
                }
            }

            "state.update" -> {
                val body = jacksonObjectMapper().readValue(payload, UpdateDto::class.java)
                if (hubs.containsKey(body.hubId)) {
                    hubs[body.hubId]!!.setStateWithUpdate(body)
                }
                getState(body.hubId)?.let {
                    publisher.publish(
                        "state.response",
                        jacksonObjectMapper().writeValueAsString(it)
                    )
                }
            }
        }
    }

    fun getState(hubId: Long): StateResponseDto? =
        hubs[hubId]?.let {
            StateResponseDto(
                hubId = it.hubId,
                state = it.getState()
            )
        }

    fun getState() = hubs.map { hub -> StateResponseDto(hub.key, hub.value.getState()) }
}