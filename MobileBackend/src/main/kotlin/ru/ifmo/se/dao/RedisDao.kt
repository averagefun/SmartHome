package ru.ifmo.se.dao

import ru.ifmo.se.dto.StateDto
import ru.ifmo.se.dto.StateRequest
import ru.ifmo.se.plugins.RedisSingleton

class RedisDao {
    fun getState(hubId: Long): StateDto? = RedisSingleton.get(hubId.toString())

    fun requestState(hubId: Long) {
        RedisSingleton.publish("state.request", StateRequest(hubId))
    }

    // suspend fun findById(id: Int): User? = DatabaseSingleton.dbQuery {
    //     Users.select(Users.id eq id)
    //         .map(::toUser)
    //         .singleOrNull()
    // }
    //
    // fun findByUsername(username: String): User? = DatabaseSingleton.dbQuery {
    //     Users.select(Users.username eq username)
    //         .map(::toUser)
    //         .singleOrNull()
    // }
    //
    // fun create(username: String, password: String): User? = DatabaseSingleton.dbQuery {
    //     val insertStatement = Users.insert {
    //         it[Users.username] = username
    //         it[Users.password] = password
    //     }
    //     insertStatement.resultedValues?.singleOrNull()?.let(::toUser)
    // }
}
