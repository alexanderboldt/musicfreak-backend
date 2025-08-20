package com.alex.musicfreak.domain.service

import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.exception.BadRequestException
import com.alex.musicfreak.mapper.plus
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.mapper.toEntity
import com.alex.musicfreak.repository.artist.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.InputStream

@ApplicationScoped
class ArtistService(
    private val minioService: MinioService,
    private val artistRepository: ArtistRepository,
    private val entityManager: EntityManager
) {

    @Transactional
    fun create(artist: Artist) = artistRepository.save(artist.toEntity()).toDomain()

    @Transactional
    fun uploadImage(id: Long, image: FileUpload?): Artist {
        // check if artist and image are existing
        val artistSaved = artistRepository.findByIdOrThrowBadRequest(id)
        if (image == null || image.uploadedFile() == null) throw BadRequestException()

        // 1. if there is already an image saved, delete it first
        artistSaved.filename?.let { minioService.deleteFile(MinioBucket.ARTIST, it) }

        // 2. upload the new image and get the filename
        val filename = minioService.uploadFile(MinioBucket.ARTIST, image)

        // 3. update the artist with the filename
        return entityManager.merge(artistSaved.copy(filename = filename)).toDomain()
    }

    @Transactional
    fun readAll(sort: Sort) = artistRepository.listAll(sort).map { it.toDomain() }

    @Transactional
    fun read(id: Long) = artistRepository.findByIdOrThrowBadRequest(id).toDomain()

    fun downloadImage(id: Long): Pair<InputStream, String> {
        // check if artist and image are existing
        val filename = artistRepository.findByIdOrThrowBadRequest(id).filename ?: throw BadRequestException()

        // download the file and return it with the filename
        return minioService.downloadFile(MinioBucket.ARTIST, filename) to filename
    }

    @Transactional
    fun update(id: Long, artistUpdate: Artist): Artist {
        val artistSaved = artistRepository.findByIdOrThrowBadRequest(id)
        return entityManager.merge(artistUpdate + artistSaved).toDomain()
    }

    @Transactional
    fun delete(id: Long) {
        val artistSaved = artistRepository.findByIdOrThrowBadRequest(id)

        // delete the image from the storage and the database-entry
        artistSaved.filename?.also { minioService.deleteFile(MinioBucket.ARTIST, it) }
        artistRepository.deleteById(id)
    }

    @Transactional
    fun deleteImage(id: Long) {
        // check if artist and image are existing
        val artistSaved = artistRepository.findByIdOrThrowBadRequest(id)
        val filename = artistSaved.filename ?: throw BadRequestException()

        // delete the file
        minioService.deleteFile(MinioBucket.ARTIST, filename)

        // update the artist by deleting the filename
        entityManager.merge(artistSaved.copy(filename = null)).toDomain()
    }
}
