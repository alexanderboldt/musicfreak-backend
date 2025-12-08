package org.musiccollection.repository

import org.musiccollection.entity.AlbumEntity
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class AlbumRepository : BaseRepository<AlbumEntity> {

    fun findByArtistId(userId: String, artistId: Long, sort: Sort): List<AlbumEntity> = list("userId = ?1 and artistId = ?2", sort, userId, artistId)
}
