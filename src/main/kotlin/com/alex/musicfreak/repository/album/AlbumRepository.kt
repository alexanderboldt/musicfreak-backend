package com.alex.musicfreak.repository.album

import com.alex.musicfreak.exception.BadRequestException
import io.quarkus.hibernate.orm.panache.PanacheRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AlbumRepository : PanacheRepository<AlbumEntity> {

    fun findByIdOrThrowBadRequest(id: Long) = findById(id) ?: throw BadRequestException()

    fun save(entity: AlbumEntity): AlbumEntity {
        persist(entity)
        return entity
    }

    fun listByArtistId(artistId: Long, sort: Sort): List<AlbumEntity> = list("artistId", sort, artistId)
}
