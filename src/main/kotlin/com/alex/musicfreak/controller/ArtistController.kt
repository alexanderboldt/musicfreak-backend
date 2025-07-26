package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.domain.service.ArtistService
import com.alex.musicfreak.util.Answer
import com.alex.musicfreak.util.Resource
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

@Path(Resource.Path.ARTISTS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ArtistController(private val artistService: ArtistService) {

    // create

    @POST
    fun post(artist: Artist) = Answer.created(artistService.create(artist))

    // read

    @GET
    fun getAll() = Answer.ok(artistService.readAll())

    @GET
    @Path(Resource.Path.ID)
    fun get(@PathParam(Resource.Param.ID) id: Long): Response {
        val artist = artistService.read(id)

        return when (artist != null) {
            true -> Answer.ok(artist)
            false -> Answer.badRequest()
        }
    }

    // update

    @PUT
    @Path(Resource.Path.ID)
    fun update(@PathParam(Resource.Param.ID) id: Long, artist: Artist): Response {
        val artistUpdated = artistService.update(id, artist)

        return when (artistUpdated != null) {
            true -> Answer.ok(artistUpdated)
            false -> Answer.badRequest()
        }
    }

    // delete

    @DELETE
    @Path(Resource.Path.ID)
    fun delete(@PathParam(Resource.Param.ID) id: Long): Response {
        return when (artistService.delete(id)) {
            true -> Answer.noContent()
            false -> Answer.badRequest()
        }
    }
}
