package com.alex.musicfreak.repository.album

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AlbumRepository : PanacheRepository<AlbumEntity> {

    fun save(entity: AlbumEntity): AlbumEntity {
        persist(entity)
        return entity
    }
}
