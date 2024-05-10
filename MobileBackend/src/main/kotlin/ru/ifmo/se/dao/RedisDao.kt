package ru.ifmo.se.dao

import io.lettuce.core.pubsub.RedisPubSubAdapter
import kotlinx.coroutines.delay
import ru.ifmo.se.dto.StateDto
import ru.ifmo.se.dto.StateRequestDto
import ru.ifmo.se.dto.StateResponseDto
import ru.ifmo.se.dto.UpdateDto
import ru.ifmo.se.plugins.RedisSingleton
import ru.ifmo.se.plugins.objectMapper
import java.time.Duration

class RedisDao {

    init {
        RedisSingleton.subscribe("state.response")
        val stateResponseListener = object : RedisPubSubAdapter<String, String>() {
            override fun message(pattern: String, channel: String, message: String) {
                val stateResponseDto = objectMapper.readValue(message, StateResponseDto::class.java)
                setState(stateResponseDto.hubId, stateResponseDto.state)
            }
        }
        RedisSingleton.addListener(stateResponseListener)
    }

    fun setState(hubId: Long, state: StateDto) {
        RedisSingleton.setWithExpiration(hubId.toString(), state, Duration.ofMinutes(1))
    }

    suspend fun tryGetState(hubId: Long, maxAttempts: Int = 5, delayMillis: Long = 1000): StateDto? {
        for (i in 1..maxAttempts) {
            val state: StateDto? = getState(hubId)
            if (state != null) {
                return state
            }

            if (i % 2 == 0) {
                requestState(hubId)
            }
            delay(delayMillis)
        }

        return null
    }

    fun getState(hubId: Long): StateDto? = RedisSingleton.get(hubId.toString())

    fun getAndInvalidateState(hubId: Long): StateDto? {
        requestState(hubId)
        return getState(hubId)
    }

    fun requestState(hubId: Long) {
        RedisSingleton.publish("state.request", StateRequestDto(hubId))
    }

    fun requestUpdate(update: UpdateDto) {
        RedisSingleton.publish("state.update", update)
    }
}
