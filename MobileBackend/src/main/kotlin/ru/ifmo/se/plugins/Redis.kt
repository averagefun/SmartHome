package ru.ifmo.se.plugins

import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection

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

    fun publish(channel: String, message: String) = redisCommands.publish(channel, message)
}
