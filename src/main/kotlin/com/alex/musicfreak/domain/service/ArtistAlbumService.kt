package com.alex.musicfreak.domain.service

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.repository.album.AlbumRepository
import com.alex.musicfreak.repository.artist.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class ArtistAlbumService(
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository
) {

    @Transactional
    fun readAll(artistId: Long, sort: Sort): List<Album> {
        artistRepository.existsOrThrowBadRequest(artistId)

        return albumRepository.listByArtistId(artistId, sort).map { it.toDomain() }
    }
}
