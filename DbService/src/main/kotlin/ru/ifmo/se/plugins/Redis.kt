package ru.ifmo.se.plugins

import io.ktor.server.application.Application
import kotlinx.coroutines.launch
import org.redisson.Redisson
import org.redisson.config.Config
import ru.ifmo.se.dao.HubDao
import ru.ifmo.se.dto.StateLogs

fun Application.configureRedis() {
    val hubDao = HubDao(environment.config.property("storage.clickhouse.url").getString())

    val config = Config()
    config.useSingleServer().address = environment.config.property("storage.redis.url").getString()
    client = Redisson.create(config).reactive()

    val topic = environment.config.property("storage.redis.topic").getString()

    client.getTopic(topic).addListener(StateLogs::class.java) { _, msg ->
        coroutineScope.launch {
            for (hubState in msg.states) {
                hubDao.saveStateEntries(hubState)
            }
        }
    }.block()
}
