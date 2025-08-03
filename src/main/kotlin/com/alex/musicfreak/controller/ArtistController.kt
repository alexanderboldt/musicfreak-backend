package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.domain.service.ArtistService
import com.alex.musicfreak.util.Answer
import com.alex.musicfreak.util.Resource
import io.quarkus.security.Authenticated
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType

@Suppress("unused")
@Path(Resource.Path.ARTIST)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
class ArtistController(private val artistService: ArtistService) {

    // create

    @POST
    fun post(artist: Artist) = Answer.created(artistService.create(artist))

    // read

    @GET
    fun getAll() = artistService.readAll()

    @GET
    @Path(Resource.Path.ID)
    fun get(@PathParam(Resource.Param.ID) id: Long) = artistService.read(id)

    // update

    @PUT
    @Path(Resource.Path.ID)
    fun update(@PathParam(Resource.Param.ID) id: Long, artist: Artist) = artistService.update(id, artist)

    // delete

    @DELETE
    @Path(Resource.Path.ID)
    fun delete(@PathParam(Resource.Param.ID) id: Long) = artistService.delete(id)
}
