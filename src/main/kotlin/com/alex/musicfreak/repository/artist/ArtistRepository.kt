package com.alex.musicfreak.repository.artist

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ArtistRepository : PanacheRepository<ArtistEntity> {

    fun notExists(id: Long) = findById(id) == null

    fun save(entity: ArtistEntity): ArtistEntity {
        persist(entity)
        return entity
    }
}
