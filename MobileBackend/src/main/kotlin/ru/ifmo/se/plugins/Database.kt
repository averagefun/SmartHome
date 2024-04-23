package ru.ifmo.se.plugins

import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ifmo.se.model.Users

fun Application.configureDatabase() {
    DatabaseSingleton.init(environment.config)
}

object DatabaseSingleton {
    fun init(config: ApplicationConfig) {
        val driver = config.property("storage.driver").getString()
        val url = config.property("storage.url").getString()
        val user = config.property("storage.user").getString()
        val password = config.property("storage.password").getString()
        val database = Database.connect(url, driver, user, password)
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
