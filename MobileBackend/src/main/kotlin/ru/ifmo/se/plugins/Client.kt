package ru.ifmo.se.plugins

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import ru.ifmo.se.dto.SensorHistoryDto
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

fun Application.configureClient() {
    ClientService.init(environment.config)
}

object ClientService {
    lateinit var dbServiceUrl: String
    lateinit var client: HttpClient

    fun init(config: ApplicationConfig) {
        dbServiceUrl = config.property("db.service.url").getString()
        client = HttpClient(CIO) {
            install(ContentNegotiation) {
                jackson {
                    registerModule(JavaTimeModule())
                    enable(SerializationFeature.INDENT_OUTPUT)
                    disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                    setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                }
            }
        }
    }

    suspend fun getSensorHistory(
        hubId: Long,
        sensorId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): SensorHistoryDto {
        val response = client.get("${dbServiceUrl}/api/state/${hubId}/${sensorId}") {
            url {
                parameters.append("from", from.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_DATE_TIME))
                parameters.append("to", to.truncatedTo(ChronoUnit.SECONDS).format(DateTimeFormatter.ISO_DATE_TIME))
            }
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                return response.body()
            }

            else -> {
                throw Exception("Failed to get sensor history, status code: ${response.status}")
            }
        }
    }
}
