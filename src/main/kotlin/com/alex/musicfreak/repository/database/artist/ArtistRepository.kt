package com.alex.musicfreak.repository.database.artist

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ArtistRepository : PanacheRepository<DbModelArtist> {

    fun exists(id: Long) = findById(id) != null

    fun save(entity: DbModelArtist): DbModelArtist {
        persist(entity)
        return entity
    }
}
