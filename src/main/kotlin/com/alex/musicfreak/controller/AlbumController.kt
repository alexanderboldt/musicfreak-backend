package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.domain.service.AlbumService
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

@Path(Resource.Path.ALBUMS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class AlbumController(private val albumService: AlbumService) {

    // create

    @POST
    fun post(album: Album): Response {
        val albumCreated = albumService.create(album)

        return when (albumCreated != null) {
            true -> Answer.created(albumCreated)
            false -> Answer.badRequest()
        }
    }

    // read

    @GET
    fun getAll() = Answer.ok(albumService.readAll())

    @GET
    @Path(Resource.Path.ID)
    fun get(@PathParam(Resource.Param.ID) id: Long): Response {
        val album = albumService.read(id)

        return when (album != null) {
            true -> Answer.ok(album)
            false -> Answer.badRequest()
        }
    }

    // update

    @PUT
    @Path(Resource.Path.ID)
    fun update(@PathParam(Resource.Param.ID) id: Long, album: Album): Response {
        val albumUpdated = albumService.update(id, album)

        return when (albumUpdated != null) {
            true -> Answer.ok(albumUpdated)
            false -> Answer.badRequest()
        }
    }

    // delete

    @DELETE
    @Path(Resource.Path.ID)
    fun delete(@PathParam(Resource.Param.ID) id: Long): Response {
        return when (albumService.delete(id)) {
            true -> Answer.noContent()
            false -> Answer.badRequest()
        }
    }
}
