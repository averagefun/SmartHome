package ru.ifmo.se.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ifmo.se.dto.StateRequestDto
import ru.ifmo.se.dto.StateResponseDto
import ru.ifmo.se.models.AbstractHub
import ru.ifmo.se.models.Hub
import ru.ifmo.se.plugins.RedisSingleton
import java.util.concurrent.ConcurrentHashMap
import kotlin.random.Random

class HubService {
    companion object {
        val current = HubService()
        @OptIn(DelicateCoroutinesApi::class)
        fun configure(){
            GlobalScope.launch {
                while (true) {
                    current.publisher.publish("scheduled.state", jacksonObjectMapper().writeValueAsString(current.getState()))
                    delay(60000)
                }
            }

        }
    }

    private val hubs: ConcurrentHashMap<Long, AbstractHub> = ConcurrentHashMap()
    init {
        (1L..10000L).forEach {
            hubs[it] = Hub(it, Random.nextLong(1, 1000))
        }
    }

    val publisher = RedisSingleton

    fun getMessage(channel: String, payload: String) {
        println("message '$payload' in chanel '$channel'")
        when(channel){
            "state.request" -> {
                val body = jacksonObjectMapper().readValue(payload, StateRequestDto::class.java)
                publisher.publish("state.response", jacksonObjectMapper().writeValueAsString(getState(body.hubId)))
            }
        }
    }

    fun getState(hubId: Long): StateResponseDto =
        hubs[hubId]?.let {
            StateResponseDto(
                hubId = it.hubId,
                stateId = it.stateId,
                state = "some state"
            )
        } ?: StateResponseDto(
            hubId = -1,
            stateId = null,
            state = "No such hub"
        )


    fun getState() = hubs.map { hub ->
        hub.value.let {
            StateResponseDto(
                hubId = it.hubId,
                stateId = it.stateId,
                state = "aboba"
        )
    }
    }

}