package com.alex.musicfreak.repository.database

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ArtistRepository : PanacheRepository<DbModelArtist> {

    fun save(entity: DbModelArtist): DbModelArtist {
        persist(entity)
        return entity
    }
}