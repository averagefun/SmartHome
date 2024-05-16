package com.example.utils

import com.example.dto.AuthDto
import com.example.dto.RequestAuthDto
import com.example.dto.RoomDto
import com.example.dto.UpdateRangeSwitchDto
import com.example.dto.UpdateSwitchDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.delay
import kotlin.random.Random

suspend fun HttpClient.getJwt(clientId: Int, baseUrl: String): String{
    var response= this.post("$baseUrl/auth/login"){
        setBody(RequestAuthDto(username = "testUser_$clientId", password = "testUser_$clientId"))
        contentType(ContentType.Application.Json)

    }
    if (response.status.value >= 400)
        response = this.post("$baseUrl/auth/register") {
            setBody(RequestAuthDto(username = "testUser_$clientId", password = "testUser_$clientId"))
            contentType(ContentType.Application.Json)
        }
    if (response.status != HttpStatusCode.OK)
        throw RuntimeException("Bad response! ${response.bodyAsText()}")
    return response.body<AuthDto>().token
}

suspend fun HttpClient.makeRequests(baseUrl: String, delaySeconds: Int, jwt: String){
    delay(delaySeconds * 1000L)
    this.get("$baseUrl/rooms") {
        contentType(ContentType.Application.Json)
        header("Authorization","Bearer $jwt")
    }

    delay(delaySeconds * 1000L)
    val room: RoomDto = this.get("$baseUrl/rooms/${Random.nextInt(1, 7)}"){
        header("Authorization","Bearer $jwt")
        contentType(ContentType.Application.Json)
    }.body()

    room.switches.takeIf { it.isNotEmpty() }?.let {
        delay(delaySeconds * 1000L)
        this.patch("$baseUrl/switches/${it.shuffled()[0].id}") {
            setBody(UpdateSwitchDto(stateId = room.stateId + 1, enabled = Random.nextBoolean()))
            header("Authorization","Bearer $jwt")
            contentType(ContentType.Application.Json)
        }
    }

    room.rangeSwitches.takeIf { it.isNotEmpty() }?.let {
        delay(delaySeconds * 1000L)
        this.patch("$baseUrl/rangeSwitches/${it.shuffled()[0].id}") {
            setBody(
                UpdateRangeSwitchDto(
                    stateId = room.stateId + 2,
                    enabled = Random.nextBoolean(),
                    value = Random.nextDouble(0.0, 100.0)
                )
            )
            header("Authorization", "Bearer $jwt")
            contentType(ContentType.Application.Json)
        }
    }
}