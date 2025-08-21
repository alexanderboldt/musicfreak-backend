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
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.InputStream
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

    @Transactional
    fun uploadImage(id: Long, image: FileUpload?): Album {
        // check if the album and the image are existing
        val albumSaved = albumRepository.findByIdOrThrowBadRequest(id)
        if (image == null || image.uploadedFile() == null) throw BadRequestException()

        // 1. if there is already an image saved, delete it first
        albumSaved.filename?.let { minioService.deleteFile(MinioBucket.ALBUM, it) }

        // 2. upload the new image and get the filename
        val filename = minioService.uploadFile(MinioBucket.ALBUM, image)

        // 3. update the album with the filename
        albumSaved.filename = filename

        return albumSaved.toDomain()
    }

    // read

    @Transactional
    fun readAll(sort: Sort) = albumRepository.listAll(sort).map { it.toDomain() }

    @Transactional
    fun read(id: Long) = albumRepository.findByIdOrThrowBadRequest(id).toDomain()

    fun downloadImage(id: Long): Pair<InputStream, String> {
        // check if the album and image are existing
        val filename = albumRepository.findByIdOrThrowBadRequest(id).filename ?: throw BadRequestException()

        // download the file and return it with the filename
        return minioService.downloadFile(MinioBucket.ALBUM, filename) to filename
    }

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
        if (!albumRepository.deleteById(id)) throw BadRequestException()
    }

    @Transactional
    fun deleteImage(id: Long) {
        // check if the album and image are existing
        val albumSaved = albumRepository.findByIdOrThrowBadRequest(id)
        val filename = albumSaved.filename ?: throw BadRequestException()

        // delete the file
        minioService.deleteFile(MinioBucket.ALBUM, filename)

        // update the album by deleting the filename
        albumSaved.filename = null
    }
}
