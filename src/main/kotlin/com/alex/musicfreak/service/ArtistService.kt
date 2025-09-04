package com.alex.musicfreak.service

import com.alex.musicfreak.domain.Artist
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.entity.ArtistEntity
import com.alex.musicfreak.repository.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import java.time.Instant

@ApplicationScoped
class ArtistService(
    private val s3Service: S3Service,
    private val userService: UserService,
    private val artistRepository: ArtistRepository
) {
    // create

    @Transactional
    fun create(artist: Artist): Artist {
        val entity = ArtistEntity(
            0,
            userService.userId,
            artist.name,
            null,
            Instant.now(),
            Instant.now()
        )
        return artistRepository.save(entity).toDomain()
    }

    // read

    @Transactional
    fun readAll(sort: Sort) = artistRepository
        .findAll(userService.userId, sort)
        .map { it.toDomain() }

    @Transactional
    fun read(id: Long) = artistRepository
        .findOrThrow(id, userService.userId)
        .toDomain()

    // update

    @Transactional
    fun update(id: Long, artistUpdate: Artist) = artistRepository
        .findOrThrow(id, userService.userId)
        .apply {
            name = artistUpdate.name
            updatedAt = Instant.now()
        }.toDomain()

    // delete

    @Transactional
    fun delete(id: Long) {
        val artistSaved = artistRepository.findOrThrow(id, userService.userId)

        // delete an existing image from the storage and the artist
        artistSaved.filename?.also { s3Service.deleteFile(S3Bucket.ARTIST, it) }
        artistRepository.deleteById(id)
    }
}
