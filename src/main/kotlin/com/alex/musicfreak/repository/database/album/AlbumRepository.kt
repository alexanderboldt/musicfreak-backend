package com.alex.musicfreak.repository.database.album

import io.quarkus.hibernate.orm.panache.PanacheRepository
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AlbumRepository : PanacheRepository<DbModelAlbum> {

    fun save(entity: DbModelAlbum): DbModelAlbum {
        persist(entity)
        return entity
    }
}
