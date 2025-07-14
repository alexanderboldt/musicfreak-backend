package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.Artist
import com.alex.musicfreak.repository.album.AlbumRepository
import com.alex.musicfreak.repository.artist.ArtistRepository
import com.alex.musicfreak.mapper.plus
import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.mapper.toEntity
import com.alex.musicfreak.util.Answer
import io.quarkus.panache.common.Sort
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("api/v1/artists")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ArtistController(
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    private val entityManager: EntityManager
) {

    private val errorId = "Artist not found!"

    // create

    @POST
    @Transactional
    fun post(artist: Artist) = Answer.created(artistRepository.save(artist.toEntity()).toDomain())

    // read

    @GET
    @Transactional
    fun getAll() = Answer.ok(artistRepository.listAll().map { it.toDomain() })

    @GET
    @Path("{id}/albums")
    @Transactional
    fun getAllAlbumsFromArtist(@PathParam("id") id: Long, @QueryParam("sort") sort: String?): Response {
        return Answer.ok(albumRepository.list("artistId", if (sort != null) Sort.by(sort) else Sort.by("year"), id).map { it.toDomain() })
    }

    @GET
    @Path("{id}")
    @Transactional
    fun get(@PathParam("id") id: Long): Response {
        return artistRepository
            .findById(id)
            ?.toDomain()
            ?.let { Answer.ok(it) }
            ?: Answer.badRequest(errorId)
    }

    // update

    @PUT
    @Path("{id}")
    @Transactional
    fun update(@PathParam("id") id: Long, artist: Artist): Response {
        val artistSaved = artistRepository.findById(id)
        return when (artistSaved != null) {
            true -> Answer.ok(entityManager.merge(artist + artistSaved))
            false -> Answer.badRequest(errorId)
        }
    }

    // delete

    @DELETE
    @Path("{id}")
    @Transactional
    fun delete(@PathParam("id") id: Long): Response {
        return when (artistRepository.deleteById(id)) {
            true -> Answer.noContent()
            false -> Answer.badRequest(errorId)
        }
    }
}
