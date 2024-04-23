package ru.ifmo.se.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.Principal
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt
import java.util.Date

const val USERNAME = "username"
const val HUB_ID = "hubId"
lateinit var jwtSecret: String

fun Application.configureSecurity() {
    jwtSecret = environment.config.property("jwt.secret").getString()

    authentication {
        jwt {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(jwtSecret))
                    .build()
            )
            validate { credential ->
                val claims = credential.payload.claims
                UserPrincipal(claims[USERNAME]!!.asString(), claims[HUB_ID]!!.asInt())
            }
        }
    }
}

class UserPrincipal(
    val username: String,
    val hubId: Int
) : Principal

fun createToken(username: String, hubId: Int): String = JWT.create()
    .withClaim(USERNAME, username)
    .withClaim(HUB_ID, hubId)
    .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
    .sign(Algorithm.HMAC256(jwtSecret))
