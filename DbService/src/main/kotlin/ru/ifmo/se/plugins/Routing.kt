package ru.ifmo.se.plugins

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitSingle
import org.redisson.Redisson
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config
import ru.ifmo.se.dao.HubDao
import ru.ifmo.se.dto.StateResponse
import ru.ifmo.se.model.HubState
import java.sql.Timestamp
import java.time.Instant
import java.time.ZoneId

lateinit var client: RedissonReactiveClient
val IODispatcher = Dispatchers.IO
val coroutineScope = CoroutineScope(IODispatcher)


fun Application.configureRouting() {
    val topic = environment.config.property("storage.redis.topic").getString()
    val hubDao = HubDao(environment.config.property("storage.clickhouse.url").getString())

    routing {
        post("/hubstate") {
            client.getTopic(topic).publish(call.receive<HubState>()).awaitSingle()
            call.respond(HttpStatusCode.Created)
        }
        get("/api/state/{hub_id}/{id}") {
            val states = hubDao.getStates(
                call.parameters["hub_id"]!!.toLong(),
                call.parameters["id"]!!.toLong(),
                call.request.queryParameters["from"],
                call.request.queryParameters["to"]
            )

            call.respond(StateResponse(states))
        }
    }
}

fun Application.configureRedis() {
    val hubDao = HubDao(environment.config.property("storage.clickhouse.url").getString())

    val config = Config()
    config.useSingleServer().address = environment.config.property("storage.redis.url").getString()
    client = Redisson.create(config).reactive()

    val topic = environment.config.property("storage.redis.topic").getString()

    client.getTopic(topic).addListener(HubState::class.java) { _, msg ->
        coroutineScope.launch {
            hubDao.saveStateEntries(msg)
        }
    }.block()
}
