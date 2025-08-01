package com.alex.musicfreak.domain.service

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.mapper.plus
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.mapper.toEntity
import com.alex.musicfreak.repository.album.AlbumRepository
import com.alex.musicfreak.repository.artist.ArtistRepository
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
    fun create(album: Album): Album? {
        if (artistRepository.notExists(album.artistId!!)) return null

        return albumRepository.save(album.toEntity()).toDomain()
    }

    @Transactional
    fun readAll() = albumRepository.listAll().map { it.toDomain() }

    @Transactional
    fun read(id: Long): Album? = albumRepository.findById(id)?.toDomain()

    @Transactional
    fun update(id: Long, album: Album): Album? {
        val albumSaved = albumRepository.findById(id)

        // check if album and artist are valid
        if (albumSaved == null) return null
        if (artistRepository.notExists(album.artistId!!)) return null

        return entityManager.merge(album + albumSaved).toDomain()
    }

    @Transactional
    fun deleteAll() = albumRepository.deleteAll()

    @Transactional
    fun delete(id: Long) = albumRepository.deleteById(id)
}
