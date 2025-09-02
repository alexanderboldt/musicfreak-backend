package com.alex.musicfreak.repository

import com.alex.musicfreak.entity.ArtistEntity
import com.alex.musicfreak.util.BadRequestException
import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ArtistRepository : PanacheRepository<ArtistEntity> {

    fun existsOrThrowBadRequest(id: Long) {
        if (findById(id) == null) throw BadRequestException()
    }

    fun findByIdOrThrowBadRequest(id: Long) = findById(id) ?: throw BadRequestException()

    fun save(entity: ArtistEntity): ArtistEntity {
        persist(entity)
        return entity
    }
}
