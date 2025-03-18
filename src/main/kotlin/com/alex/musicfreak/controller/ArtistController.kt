package com.alex.musicfreak.controller

import com.alex.musicfreak.repository.api.ApiModelArtistPost
import com.alex.musicfreak.repository.database.ArtistRepository
import com.alex.musicfreak.repository.mapping.toApiModelGet
import com.alex.musicfreak.repository.mapping.toDbModel
import com.alex.musicfreak.util.badRequest
import com.alex.musicfreak.util.created
import com.alex.musicfreak.util.noContent
import com.alex.musicfreak.util.ok
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
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("api/v1/artists")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ArtistController(
    private val artistRepository: ArtistRepository,
    private val entityManager: EntityManager
) {

    // create

    @POST
    @Transactional
    fun post(artist: ApiModelArtistPost): Response {
        artistRepository.persist(artist.toDbModel())
        return created(artist)
    }

    // read

    @GET
    @Transactional
    fun getAll() = artistRepository.listAll().toApiModelGet()

    @GET
    @Path("{id}")
    @Transactional
    fun get(@PathParam("id") id: Long): Response {
        return artistRepository
            .findById(id)
            ?.toApiModelGet()
            ?.let { ok(it) }
            ?: badRequest("id not found")
    }

    // update

    @PUT
    @Path("{id}")
    @Transactional
    fun update(@PathParam("id") id: Long, artist: ApiModelArtistPost): Response {
        val artistSaved = artistRepository.findById(id)
        return when (artistSaved != null) {
            true -> ok(entityManager.merge(artist.toDbModel(artistSaved)))
            false -> badRequest("id not found")
        }
    }

    // delete

    @DELETE
    @Path("{id}")
    @Transactional
    fun delete(@PathParam("id") id: Long): Response {
        return when (artistRepository.deleteById(id)) {
            true -> noContent()
            false -> badRequest("id not found")
        }
    }
}