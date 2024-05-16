package com.example

import com.example.dto.AuthDto
import com.example.dto.RequestAuthDto
import com.example.dto.RoomDto
import com.example.dto.UpdateRangeSwitchDto
import com.example.dto.UpdateSwitchDto
import com.example.utils.getJwt
import com.example.utils.makeRequests
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.http.headers
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.ktor.server.netty.*
import kotlinx.serialization.json.Json
import kotlin.random.Random

//suspend fun

fun main(args: Array<String>) = EngineMain.main(args)
fun Application.module() {
    val delaySeconds = environment.config.property("requestMaker.delay").getString().toInt()
    val clientsCount = environment.config.property("requestMaker.clientsCount").getString().toInt()
    val baseUrl = environment.config.property("requestMaker.url").getString()
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
    (1..clientsCount).forEach { clientId ->
        launch {
            delay(20000)
            val jwt = client.getJwt(clientId, baseUrl)
            while (true){
                client.makeRequests(baseUrl, delaySeconds, jwt)
            }
        }
    }
}