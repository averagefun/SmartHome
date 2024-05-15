package com.example

import com.example.dto.AuthDto
import com.example.dto.RoomDto
import com.example.dto.UpdateRangeSwitchDto
import com.example.dto.UpdateSwitchDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.call.receive
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import kotlin.random.Random

//suspend fun

fun main(args: Array<String>) = EngineMain.main(args)
fun Application.module() {
//    (1..clientsCount).forEach { clientId ->
//        launch {
//            var response= client.post("$baseUrl/auth/login"){
//                setBody("{\n" +
//                        "  \"username\": \"testUser$clientId\",\n" +
//                        "  \"password\": \"testUser$clientId\"\n" +
//                        "}\n")
//            }
//            if (response.status.value >= 400)
//                response = client.post("$baseUrl/auth/register") {
//                    setBody("{\n" +
//                            "  \"username\": \"testUser$clientId\",\n" +
//                            "  \"password\": \"testUser$clientId\"\n" +
//                            "}\n")
//                }
//            if (response.status != HttpStatusCode.OK)
//                throw RuntimeException("Bad response!")
//            val body: AuthDto = response.body()
//
//            while (true){
//                delay(delaySeconds * 1000L)
//                client.get("$baseUrl/rooms")
//                delay(delaySeconds * 1000L)
//                val room: RoomDto = client.get("$baseUrl/rooms/${Random.nextInt(1, 7)}"){
//                    headers {
//                        append(HttpHeaders.Authorization, "Bearer: ${body.token}")
//                    }
//                }.body()
//
//                delay(delaySeconds * 1000L)
//                client.patch("$baseUrl/switches/${room.switches.shuffled()[0].id}") {
//                    setBody(UpdateSwitchDto(stateId = room.stateId + 1, enabled = Random.nextBoolean()))
//                    headers {
//                        append(HttpHeaders.Authorization, "Bearer: ${body.token}")
//                    }
//                }
//
//                delay(delaySeconds * 1000L)
//                client.patch("$baseUrl/rangeSwitches/${room.rangeSwitches.shuffled()[0].id}") {
//                    setBody(UpdateRangeSwitchDto(stateId = room.stateId + 2, enabled = Random.nextBoolean(), value = Random.nextDouble(0.0, 100.0)))
//                    headers {
//                        append(HttpHeaders.Authorization, "Bearer: ${body.token}")
//                    }
//                }
//
//            }
//
//        }
//    }
}

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}


val delaySeconds = 5
val clientsCount = 5
val baseUrl = "localhost"