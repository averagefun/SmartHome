package ru.ifmo.se.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.server.application.*
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
        val current = HubService()

        @OptIn(DelicateCoroutinesApi::class)
        fun configure() {
            GlobalScope.launch {
                while (true) {
                    current.publisher.publish(
                        "state.logs",
                        jacksonObjectMapper().writeValueAsString(current.getState())
                    )
                    delay(60000)
                }
            }
        }
    }

    private val hubs: ConcurrentHashMap<Long, AbstractHub> = ConcurrentHashMap()

    init {
        (1L..100L).forEach {
            hubs[it] = SmartHomeHub(it)
        }
        startSumulation()
    }

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

    fun getState() = hubs.map { hub -> hub.value.getState() }
}