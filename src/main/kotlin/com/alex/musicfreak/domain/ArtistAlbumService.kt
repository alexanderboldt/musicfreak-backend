package com.alex.musicfreak.domain

import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.repository.album.AlbumRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class ArtistAlbumService(private val albumRepository: AlbumRepository) {

    @Transactional
    fun readAll(artistId: Long, sort: String?): List<Album> {
        return albumRepository.list("artistId", if (sort != null) Sort.by(sort) else Sort.by("year"), artistId).map { it.toDomain() }
    }
}