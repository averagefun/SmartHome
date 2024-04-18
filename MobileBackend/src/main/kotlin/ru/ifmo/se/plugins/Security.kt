package ru.ifmo.se.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.Principal
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt
import java.util.Date

const val USERNAME = "username"
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
                UserPrincipal(claims[USERNAME]!!.asString())
            }
        }
    }
}

class UserPrincipal(
    val username: String
) : Principal

fun createToken(username: String): String = JWT.create()
    .withClaim(USERNAME, username)
    .withExpiresAt(Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
    .sign(Algorithm.HMAC256(jwtSecret))
