package com.alex.musicfreak.domain.service

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.exception.BadRequestException
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.mapper.toEntity
import com.alex.musicfreak.repository.album.AlbumRepository
import com.alex.musicfreak.repository.artist.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.sql.Timestamp
import java.time.Instant

@ApplicationScoped
class AlbumService(
    private val minioService: MinioService,
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository
) {
    // create

    @Transactional
    fun create(album: Album): Album {
        artistRepository.existsOrThrowBadRequest(album.artistId)

        return albumRepository.save(album.toEntity()).toDomain()
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
                updatedAt = Timestamp.from(Instant.now())
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
        albumSaved.filename?.also { minioService.deleteFile(MinioBucket.ALBUM, it) }
        albumRepository.deleteById(id)
    }
}
