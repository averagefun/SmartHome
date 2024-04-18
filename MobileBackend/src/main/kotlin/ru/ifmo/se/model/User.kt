package ru.ifmo.se.model

import org.jetbrains.exposed.sql.Table

data class User(val id: Int, val username: String, val password: String)

object Users : Table() {
    val id = integer("id").autoIncrement()
    val username = varchar("username", 128)
    val password = varchar("password", 512)

    override val primaryKey = PrimaryKey(id)
}
