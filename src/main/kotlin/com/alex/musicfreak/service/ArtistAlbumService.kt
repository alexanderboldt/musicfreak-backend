package com.alex.musicfreak.service

import com.alex.musicfreak.domain.AlbumResponse
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.repository.AlbumRepository
import com.alex.musicfreak.repository.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional

@ApplicationScoped
class ArtistAlbumService(
    private val userService: UserService,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository
) {

    @Transactional
    fun readAll(artistId: Long, sort: Sort): List<AlbumResponse> {
        artistRepository.existsOrThrow(artistId, userService.userId)

        return albumRepository
            .findByArtistId(userService.userId, artistId, sort)
            .map { it.toDomain() }
    }
}
