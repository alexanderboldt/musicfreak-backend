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
        val artistSaved = artistRepository.findById(id) ?: throw BadRequestException()
        if (image == null || image.uploadedFile() == null) throw BadRequestException()

        // upload the file and get the path of the image
        val imagePath = minioService.uploadFile(image)

        // update the artist with the image-path
        return entityManager.merge(artistSaved.copy(imagePath = imagePath)).toDomain()
    }

    @Transactional
    fun readAll(sort: Sort) = artistRepository.listAll(sort).map { it.toDomain() }

    @Transactional
    fun read(id: Long) = artistRepository.findById(id)?.toDomain() ?: throw BadRequestException()

    @Transactional
    fun update(id: Long, artistUpdate: Artist): Artist {
        val artistSaved = artistRepository.findById(id) ?: throw BadRequestException()
        return entityManager.merge(artistUpdate + artistSaved).toDomain()
    }

    @Transactional
    fun delete(id: Long) {
        if (!artistRepository.deleteById(id)) throw BadRequestException()
    }
}
