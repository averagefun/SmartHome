package ru.ifmo.se.plugins

import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.pubsub.RedisPubSubListener
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import java.time.Duration

fun Application.configureRedis() {
    RedisSingleton.init(environment.config)
}

object RedisSingleton {
    lateinit var redisClient: RedisClient
    lateinit var redisSubConnection: StatefulRedisPubSubConnection<String, String>
    lateinit var redisCommands: RedisCommands<String, String>

    fun init(config: ApplicationConfig) {
        val url = config.property("redis.url").getString()
        redisClient = RedisClient.create(url)
        redisSubConnection = redisClient.connectPubSub()
        redisCommands = redisClient.connect().sync()
    }

    inline fun <reified T> get(key: String): T? =
        if (redisCommands.exists(key) > 0) objectMapper.readValue(redisCommands.get(key), T::class.java) else null

    fun setWithExpiration(key: String, content: Any, invalidateAt: Duration) {
        redisCommands.psetex(key, invalidateAt.toMillis(), objectMapper.writeValueAsString(content))
    }

    fun publish(channel: String, message: Any) {
        redisCommands.publish(channel, objectMapper.writeValueAsString(message))
    }

    fun subscribe(pattern: String) {
        redisSubConnection.async().psubscribe(pattern)
    }

    fun addListener(listener: RedisPubSubListener<String, String>) {
        redisSubConnection.addListener(listener)
    }
}
