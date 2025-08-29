package com.alex.musicfreak.service

import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.exception.BadRequestException
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.repository.AlbumRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.transaction.Transactional
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.InputStream

@ApplicationScoped
class AlbumImageService(
    private val s3Service: S3Service,
    private val albumRepository: AlbumRepository
) {

    @Transactional
    fun uploadImage(id: Long, image: FileUpload?): Album {
        // check if the album and the image are existing
        val albumSaved = albumRepository.findByIdOrThrowBadRequest(id)
        if (image == null || image.uploadedFile() == null) throw BadRequestException()

        // 1. if there is already an image saved, delete it first
        albumSaved.filename?.let { s3Service.deleteFile(S3Bucket.ALBUM, it) }

        // 2. upload the new image and get the filename
        val filename = s3Service.uploadFile(S3Bucket.ALBUM, image.uploadedFile(), image.fileName())

        // 3. update the album with the filename
        albumSaved.filename = filename

        return albumSaved.toDomain()
    }

    fun downloadImage(id: Long): Pair<InputStream, String> {
        // check if the album and image are existing
        val filename = albumRepository.findByIdOrThrowBadRequest(id).filename ?: throw BadRequestException()

        // download the file and return it with the filename
        return s3Service.downloadFile(S3Bucket.ALBUM, filename) to filename
    }

    @Transactional
    fun deleteImage(id: Long) {
        // check if the album and image are existing
        val albumSaved = albumRepository.findByIdOrThrowBadRequest(id)
        val filename = albumSaved.filename ?: throw BadRequestException()

        // delete the file
        s3Service.deleteFile(S3Bucket.ALBUM, filename)

        // update the album by deleting the filename
        albumSaved.filename = null
    }
}
