package ru.ifmo.se.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ifmo.se.dto.ChangeStateDto
import ru.ifmo.se.dto.StateRequestDto
import ru.ifmo.se.dto.StateResponseDto
import ru.ifmo.se.models.AbstractHub
import ru.ifmo.se.models.Hub
import ru.ifmo.se.models.stateExample
import ru.ifmo.se.plugins.RedisSingleton
import java.util.concurrent.ConcurrentHashMap

class HubService {
    companion object {
        val current = HubService()
        @OptIn(DelicateCoroutinesApi::class)
        fun configure(){
            GlobalScope.launch {
                while (true) {
                    current.publisher.publish("state.logs", jacksonObjectMapper().writeValueAsString(current.getState()))
                    delay(60000)
                }
            }

        }
    }

    private val hubs: ConcurrentHashMap<Long, AbstractHub> = ConcurrentHashMap()
    init {
        (1L..10000L).forEach {
            hubs[it] = Hub(it)
        }
    }

    val publisher = RedisSingleton

    fun getMessage(channel: String, payload: String) {
        println("message '$payload' in chanel '$channel'")
        when(channel){
            "state.request" -> {
                val body = jacksonObjectMapper().readValue(payload, StateRequestDto::class.java)
                getState(body.hubId)?.copy(
                    state = stateExample,
                )?.let { publisher.publish("state.response", jacksonObjectMapper().writeValueAsString(it)) }            }
            "state.update" -> {
                val body = jacksonObjectMapper().readValue(payload, ChangeStateDto::class.java)
                getState(body.hubId)
                    //?.takeIf { "update only if more then hub.state.id" != null }
                    ?.copy( // fixme
                    state = stateExample,
                )?.let { publisher.publish("state.response", jacksonObjectMapper().writeValueAsString(it)) }
            }
        }
    }

    fun getState(hubId: Long): StateResponseDto? =
        hubs[hubId]?.let {
            StateResponseDto(
                hubId = it.hubId,
                state = it.state
            )
        }


    fun getState() = hubs.map { hub -> hub.value.state }
}