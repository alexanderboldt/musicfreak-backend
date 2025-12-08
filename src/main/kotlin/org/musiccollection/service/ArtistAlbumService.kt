package org.musiccollection.service

import org.musiccollection.domain.AlbumResponse
import org.musiccollection.mapper.toDomain
import org.musiccollection.repository.AlbumRepository
import org.musiccollection.repository.ArtistRepository
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
