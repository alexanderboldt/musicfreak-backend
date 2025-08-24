package com.alex.musicfreak.domain.service

import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.mapper.toEntity
import com.alex.musicfreak.repository.artist.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.sql.Timestamp
import java.time.Instant

@ApplicationScoped
class ArtistService(
    private val minioService: MinioService,
    private val artistRepository: ArtistRepository
) {
    // create

    @Transactional
    fun create(artist: Artist) = artistRepository.save(artist.toEntity()).toDomain()

    // read

    @Transactional
    fun readAll(sort: Sort) = artistRepository.listAll(sort).map { it.toDomain() }

    @Transactional
    fun read(id: Long) = artistRepository.findByIdOrThrowBadRequest(id).toDomain()

    // update

    @Transactional
    fun update(id: Long, artistUpdate: Artist): Artist {
        return artistRepository
            .findByIdOrThrowBadRequest(id)
            .apply {
                name = artistUpdate.name
                updatedAt = Timestamp.from(Instant.now())
            }.toDomain()
    }

    // delete

    @Transactional
    fun delete(id: Long) {
        val artistSaved = artistRepository.findByIdOrThrowBadRequest(id)

        // delete the image from the storage and the database-entry
        artistSaved.filename?.also { minioService.deleteFile(MinioBucket.ARTIST, it) }
        artistRepository.deleteById(id)
    }
}
