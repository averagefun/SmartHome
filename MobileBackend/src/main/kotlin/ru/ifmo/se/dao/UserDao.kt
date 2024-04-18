package ru.ifmo.se.dao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import ru.ifmo.se.dao.DatabaseSingleton.dbQuery
import ru.ifmo.se.model.User
import ru.ifmo.se.model.Users

class UserDao : DAOFacade<User> {
    private fun toUser(row: ResultRow) = User(
        id = row[Users.id],
        username = row[Users.username],
        password = row[Users.password]
    )

    override suspend fun findAll(): List<User> = dbQuery {
        Users.selectAll().map(::toUser)
    }

    override suspend fun findById(id: Int): User? = dbQuery {
        Users.select(Users.id eq id)
            .map(::toUser)
            .singleOrNull()
    }

    suspend fun findByUsername(username: String): User? = dbQuery {
        Users.select(Users.username eq username)
            .map(::toUser)
            .singleOrNull()
    }

    suspend fun create(username: String, password: String): User? = dbQuery {
        val insertStatement = Users.insert {
            it[Users.username] = username
            it[Users.password] = password
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::toUser)
    }
}

val userDao: UserDao = UserDao()
