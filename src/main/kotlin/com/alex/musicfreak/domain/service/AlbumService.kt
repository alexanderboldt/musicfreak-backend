package com.alex.musicfreak.domain.service

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.repository.album.AlbumEntity
import com.alex.musicfreak.repository.album.AlbumRepository
import com.alex.musicfreak.repository.artist.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.Instant

@ApplicationScoped
class AlbumService(
    private val s3Service: S3Service,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository
) {
    // create

    @Transactional
    fun create(album: Album): Album {
        artistRepository.existsOrThrowBadRequest(album.artistId)

        val entity = AlbumEntity(
            0,
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
    fun readAll(sort: Sort) = albumRepository.listAll(sort).map { it.toDomain() }

    @Transactional
    fun read(id: Long) = albumRepository.findByIdOrThrowBadRequest(id).toDomain()

    // update

    @Transactional
    fun update(id: Long, albumUpdate: Album): Album {
        artistRepository.existsOrThrowBadRequest(albumUpdate.artistId)

        return albumRepository
            .findByIdOrThrowBadRequest(id)
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
        val albumSaved = albumRepository.findByIdOrThrowBadRequest(id)

        // delete an existing image from the storage and the album
        albumSaved.filename?.also { s3Service.deleteFile(S3Bucket.ALBUM, it) }
        albumRepository.deleteById(id)
    }
}
