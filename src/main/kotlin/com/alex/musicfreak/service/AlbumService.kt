package com.alex.musicfreak.service

import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.entity.AlbumEntity
import com.alex.musicfreak.repository.AlbumRepository
import com.alex.musicfreak.repository.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.Instant

@ApplicationScoped
class AlbumService(
    private val s3Service: S3Service,
    private val userService: UserService,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository
) {
    // create

    @Transactional
    fun create(album: Album): Album {
        artistRepository.existsOrThrow(album.artistId, userService.userId)

        val entity = AlbumEntity(
            0,
            userService.userId,
            album.artistId,
            album.name,
            album.year,
            album.tracks,
            null,
            Instant.now(),
            Instant.now()
        )

        return albumRepository.save(entity).toDomain()
    }

    // read

    @Transactional
    fun readAll(sort: Sort) = albumRepository
        .findAll(userService.userId, sort)
        .map { it.toDomain() }

    @Transactional
    fun read(id: Long) = albumRepository
        .findOrThrow(id, userService.userId)
        .toDomain()

    // update

    @Transactional
    fun update(id: Long, albumUpdate: Album): Album {
        artistRepository.existsOrThrow(albumUpdate.artistId, userService.userId)

        return albumRepository
            .findOrThrow(id, userService.userId)
            .apply {
                artistId = albumUpdate.artistId
                name = albumUpdate.name
                year = albumUpdate.year
                tracks = albumUpdate.tracks
                updatedAt = Instant.now()
            }.toDomain()
    }

    // delete

    @Transactional
    fun deleteAll() {
        albumRepository.deleteAll()
    }

    @Transactional
    fun delete(id: Long) {
        val albumSaved = albumRepository.findOrThrow(id, userService.userId)

        // delete an existing image from the storage and the album
        albumSaved.filename?.also { s3Service.deleteFile(S3Bucket.ALBUM, it) }
        albumRepository.deleteById(id)
    }
}
