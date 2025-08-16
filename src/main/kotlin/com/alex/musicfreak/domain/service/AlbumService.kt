package com.alex.musicfreak.domain.service

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.exception.BadRequestException
import com.alex.musicfreak.mapper.plus
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.mapper.toEntity
import com.alex.musicfreak.repository.album.AlbumRepository
import com.alex.musicfreak.repository.artist.ArtistRepository
import io.quarkus.panache.common.Sort
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

@ApplicationScoped
class AlbumService(
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    private val entityManager: EntityManager
) {

    @Transactional
    fun create(album: Album): Album {
        artistRepository.existsOrThrowBadRequest(album.artistId!!)

        return albumRepository.save(album.toEntity()).toDomain()
    }

    @Transactional
    fun readAll(sort: Sort) = albumRepository.listAll(sort).map { it.toDomain() }

    @Transactional
    fun read(id: Long) = albumRepository.findByIdOrThrowBadRequest(id).toDomain()

    @Transactional
    fun update(id: Long, albumUpdate: Album): Album {
        val albumSaved = albumRepository.findByIdOrThrowBadRequest(id)

        artistRepository.existsOrThrowBadRequest(albumUpdate.artistId!!)

        return entityManager.merge(albumUpdate + albumSaved).toDomain()
    }

    @Transactional
    fun deleteAll() {
        albumRepository.deleteAll()
    }

    @Transactional
    fun delete(id: Long) {
        if (!albumRepository.deleteById(id)) throw BadRequestException()
    }
}
