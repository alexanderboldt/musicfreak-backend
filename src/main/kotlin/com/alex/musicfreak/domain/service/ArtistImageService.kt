package com.alex.musicfreak.domain.service

import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.exception.BadRequestException
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.repository.artist.ArtistRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.InputStream

@ApplicationScoped
class ArtistImageService(
    private val s3Service: S3Service,
    private val artistRepository: ArtistRepository
) {

    @Transactional
    fun uploadImage(id: Long, image: FileUpload?): Artist {
        // check if the artist and the image are existing
        val artistSaved = artistRepository.findByIdOrThrowBadRequest(id)
        if (image == null || image.uploadedFile() == null) throw BadRequestException()

        // 1. if there is already an image saved, delete it first
        artistSaved.filename?.let { s3Service.deleteFile(S3Bucket.ARTIST, it) }

        // 2. upload the new image and get the filename
        val filename = s3Service.uploadFile(S3Bucket.ARTIST, image.uploadedFile(), image.fileName())

        // 3. update the artist with the filename
        artistSaved.filename = filename

        return artistSaved.toDomain()
    }

    fun downloadImage(id: Long): Pair<InputStream, String> {
        // check if the artist and image are existing
        val filename = artistRepository.findByIdOrThrowBadRequest(id).filename ?: throw BadRequestException()

        // download the file and return it with the filename
        return s3Service.downloadFile(S3Bucket.ARTIST, filename) to filename
    }

    @Transactional
    fun deleteImage(id: Long) {
        // check if the artist and image are existing
        val artistSaved = artistRepository.findByIdOrThrowBadRequest(id)
        val filename = artistSaved.filename ?: throw BadRequestException()

        // delete the file
        s3Service.deleteFile(S3Bucket.ARTIST, filename)

        // update the artist by deleting the filename
        artistSaved.filename = null
    }
}
