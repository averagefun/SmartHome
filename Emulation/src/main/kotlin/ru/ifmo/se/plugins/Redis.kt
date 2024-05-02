package ru.ifmo.se.plugins

import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import io.lettuce.core.RedisClient
import io.lettuce.core.api.sync.RedisCommands
import io.lettuce.core.pubsub.RedisPubSubListener
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection
import ru.ifmo.se.service.HubService

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
        subscribe()
    }

    fun publish(channel: String, message: String) = redisCommands.publish(channel, message)

    val listener = object : RedisPubSubListener<String, String> {
        override fun message(channel: String, message: String) {
            println("Received message: $message from channel: $channel")
            HubService.current.getMessage(channel, message)
        }

        override fun message(pattern: String?, channel: String?, message: String?) {
            println("Something happened")
        }

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
        redisSubConnection.async().psubscribe("*")
    }
}
