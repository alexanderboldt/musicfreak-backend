package com.alex.musicfreak.controller

import com.alex.musicfreak.repository.api.ApiModelArtist
import com.alex.musicfreak.repository.database.album.AlbumRepository
import com.alex.musicfreak.repository.database.artist.ArtistRepository
import com.alex.musicfreak.repository.mapper.mergeDbModel
import com.alex.musicfreak.repository.mapper.newDbModel
import com.alex.musicfreak.repository.mapper.toApiModel
import com.alex.musicfreak.repository.mapper.toApiModels
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
    fun post(artist: ApiModelArtist) = Answer.created(artistRepository.save(artist.newDbModel()).toApiModel())

    // read

    @GET
    @Transactional
    fun getAll() = Answer.ok(artistRepository.listAll().toApiModels())

    @GET
    @Path("{id}/albums")
    @Transactional
    fun getAllAlbumsFromArtist(@PathParam("id") id: Long, @QueryParam("sort") sort: String?): Response {
        return Answer.ok(albumRepository.list("artistId", if (sort != null) Sort.by(sort) else Sort.by("year"), id).toApiModels())
    }

    @GET
    @Path("{id}")
    @Transactional
    fun get(@PathParam("id") id: Long): Response {
        return artistRepository
            .findById(id)
            ?.toApiModel()
            ?.let { Answer.ok(it) }
            ?: Answer.badRequest(errorId)
    }

    // update

    @PUT
    @Path("{id}")
    @Transactional
    fun update(@PathParam("id") id: Long, artist: ApiModelArtist): Response {
        val artistSaved = artistRepository.findById(id)
        return when (artistSaved != null) {
            true -> Answer.ok(entityManager.merge(artist.mergeDbModel(artistSaved)))
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