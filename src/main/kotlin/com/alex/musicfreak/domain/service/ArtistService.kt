package com.alex.musicfreak.domain.service

import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.repository.artist.ArtistEntity
import com.alex.musicfreak.repository.artist.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.Instant

@ApplicationScoped
class ArtistService(
    private val s3Service: S3Service,
    private val artistRepository: ArtistRepository
) {
    // create

    @Transactional
    fun create(artist: Artist): Artist {
        val entity = ArtistEntity(
            0,
            artist.name,
            null,
            Instant.now(),
            Instant.now()
        )

        return artistRepository.save(entity).toDomain()
    }

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
                updatedAt = Instant.now()
            }.toDomain()
    }

    // delete

    @Transactional
    fun delete(id: Long) {
        val artistSaved = artistRepository.findByIdOrThrowBadRequest(id)

        // delete an existing image from the storage and the artist
        artistSaved.filename?.also { s3Service.deleteFile(S3Bucket.ARTIST, it) }
        artistRepository.deleteById(id)
    }
}
