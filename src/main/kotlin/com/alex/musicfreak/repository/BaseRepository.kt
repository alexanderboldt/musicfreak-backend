package com.alex.musicfreak.repository

import com.alex.musicfreak.util.BadRequestException
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.panache.common.Sort

interface BaseRepository<T> : PanacheRepository<T> {

    // create

    fun save(entity: T): T {
        persist(entity)
        return entity
    }

    // read

    fun existsOrThrow(id: Long, userId: String) {
        if (count("id = ?1 and userId = ?2", id, userId) == 0L) throw BadRequestException()
    }

    fun findAll(userId: String, sort: Sort): List<T> = list("userId", sort, userId)

    fun findOrThrow(id: Long, userId: String): T {
        return find("id = ?1 and userId = ?2", id, userId)
            .firstResult()
            ?: throw BadRequestException()
    }
}
