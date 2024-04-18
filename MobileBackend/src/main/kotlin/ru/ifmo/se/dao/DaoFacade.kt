package ru.ifmo.se.dao

interface DAOFacade<T> {
    suspend fun findAll(): List<T>
    suspend fun findById(id: Int): T?
}