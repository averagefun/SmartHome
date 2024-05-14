package com.example

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.application.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.ktor.server.netty.*

fun main(args: Array<String>) = EngineMain.main(args)
fun Application.module() {
    GlobalScope.launch {
        while (true) {
            println("aboba")
            delay(6000)
        }
    }
}

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
        })
    }
}
