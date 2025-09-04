package com.alex.musicfreak.service

import com.alex.musicfreak.domain.Artist
import com.alex.musicfreak.util.BadRequestException
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.repository.ArtistRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.InputStream
import java.time.Instant

@ApplicationScoped
class ArtistImageService(
    private val s3Service: S3Service,
    private val userService: UserService,
    private val artistRepository: ArtistRepository
) {

    @Transactional
    fun uploadImage(id: Long, image: FileUpload?): Artist {
        // check if the artist and the image are existing
        val artistSaved = artistRepository.findOrThrow(id, userService.userId)
        if (image == null || image.uploadedFile() == null) throw BadRequestException()

        // 1. if there is already an image saved, delete it first
        artistSaved.filename?.let { s3Service.deleteFile(S3Bucket.ARTIST, it) }

        // 2. upload the new image and get the filename
        val filename = s3Service.uploadFile(S3Bucket.ARTIST, image.uploadedFile(), image.fileName())

        // 3. update the artist and return it
        return artistSaved
            .also {
                it.filename = filename
                it.updatedAt = Instant.now()
            }.toDomain()
    }

    fun downloadImage(id: Long): Pair<InputStream, String> {
        // check if the artist and image are existing
        val filename = artistRepository.findOrThrow(id, userService.userId).filename ?: throw BadRequestException()

        // download the file and return it with the filename
        return s3Service.downloadFile(S3Bucket.ARTIST, filename) to filename
    }

    @Transactional
    fun deleteImage(id: Long) {
        // check if the artist and image are existing
        val artistSaved = artistRepository.findOrThrow(id, userService.userId)
        val filename = artistSaved.filename ?: throw BadRequestException()

        // delete the file
        s3Service.deleteFile(S3Bucket.ARTIST, filename)

        // update the artist
        artistSaved.also {
            it.filename = null
            it.updatedAt = Instant.now()
        }
    }
}
