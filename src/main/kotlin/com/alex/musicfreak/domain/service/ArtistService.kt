package com.alex.musicfreak.domain.service

import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.mapper.plus
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.mapper.toEntity
import com.alex.musicfreak.repository.artist.ArtistRepository
import jakarta.enterprise.context.ApplicationScoped
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional

@ApplicationScoped
class ArtistService(
    private val artistRepository: ArtistRepository,
    private val entityManager: EntityManager
) {

    @Transactional
    fun create(artist: Artist) = artistRepository.save(artist.toEntity()).toDomain()

    @Transactional
    fun readAll() = artistRepository.listAll().map { it.toDomain() }

    @Transactional
    fun read(id: Long): Artist? = artistRepository.findById(id)?.toDomain()

    @Transactional
    fun update(id: Long, artistUpdate: Artist): Artist? {
        val artistSaved = artistRepository.findById(id)
        if (artistSaved == null) return null

        return entityManager.merge(artistUpdate + artistSaved).toDomain()
    }

    @Transactional
    fun delete(id: Long) = artistRepository.deleteById(id)
}
