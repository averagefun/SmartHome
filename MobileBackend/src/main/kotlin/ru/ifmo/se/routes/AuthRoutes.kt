package ru.ifmo.se.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import org.mindrot.jbcrypt.BCrypt
import ru.ifmo.se.dao.UserDao
import ru.ifmo.se.dto.AuthRequest
import ru.ifmo.se.dto.AuthResponse
import ru.ifmo.se.dto.ErrorResponse
import ru.ifmo.se.plugins.createToken

fun Application.authRoutes() {
    val userDao = UserDao()

    routing {
        route("/api/auth") {
            post("/register") {
                val auth = call.receive<AuthRequest>()
                if (userDao.findByUsername(auth.username) != null) {
                    call.respond(HttpStatusCode.BadRequest, ErrorResponse("User '${auth.username}' already exists"))
                } else {
                    userDao.create(auth.username, BCrypt.hashpw(auth.password, BCrypt.gensalt()))
                        ?.let { user -> createToken(user.username, user.id) }
                        ?.let { token -> call.respond(AuthResponse(token)) }
                        ?: call.respond(HttpStatusCode.InternalServerError, ErrorResponse("Failed to register user"))
                }
            }
            post("/login") {
                val auth = call.receive<AuthRequest>()
                val user = userDao.findByUsername(auth.username)
                    ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("User '${auth.username}' not exists")
                    )
                if (BCrypt.checkpw(auth.password, user.password)) {
                    createToken(user.username, user.id).let { token -> call.respond(AuthResponse(token)) }
                }
                return@post call.respond(HttpStatusCode.BadRequest, ErrorResponse("Wrong password"))
            }
        }
    }
}
