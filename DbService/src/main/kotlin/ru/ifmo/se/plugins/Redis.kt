package ru.ifmo.se.plugins

import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.pubsub.RedisPubSubListener
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import ru.ifmo.se.dao.HubDao
import ru.ifmo.se.model.HubState
import ru.ifmo.se.objectMapper
import com.fasterxml.jackson.module.kotlin.readValue

fun Application.configureRedis() {
    RedisSingleton.init(environment.config)
}

object RedisSingleton {
    lateinit var redisClient: RedisClient
    lateinit var redisSubConnection: StatefulRedisPubSubConnection<String, String>
    lateinit var redisCommands: RedisCommands<String, String>
    lateinit var hubDao: HubDao

    fun init(config: ApplicationConfig) {
        val url = config.property("storage.redis.url").getString()
        hubDao = HubDao(config.property("storage.clickhouse.url").getString())
        redisClient = RedisClient.create(url)
        redisSubConnection = redisClient.connectPubSub()
        redisSubConnection = redisClient.connectPubSub()
        redisCommands = redisClient.connect().sync()
        subscribe()
    }

    private val listener = object : RedisPubSubListener<String, String> {
        override fun message(channel: String, message: String) {
        }

        override fun message(pattern: String?, channel: String?, message: String?): Unit =
            channel?.let {
                val hubStates: List<HubState> = message?.let { it1 -> objectMapper.readValue(it1) } ?: emptyList()
                hubDao.saveStateEntries(hubStates)
            } ?: Unit

        override fun subscribed(channel: String?, count: Long) {
        }

        override fun psubscribed(pattern: String?, count: Long) {
        }

        override fun unsubscribed(channel: String?, count: Long) {
        }

        override fun punsubscribed(pattern: String?, count: Long) {
        }
    }


    fun subscribe(){
        redisSubConnection.addListener(listener)
        redisSubConnection.async().psubscribe("state.logs")
    }
}
