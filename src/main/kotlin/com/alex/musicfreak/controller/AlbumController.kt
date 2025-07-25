package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.domain.AlbumService
import com.alex.musicfreak.util.Answer
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

@Path("api/v1/albums")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class AlbumController(private val albumService: AlbumService) {

    private val errorId = "Album not found!"
    private val errorArtistId = "Artist not found!"

    // create

    @POST
    fun post(album: Album): Response {
        val albumCreated = albumService.create(album)

        return when (albumCreated != null) {
            true -> Answer.created(albumCreated)
            false -> Answer.badRequest(errorArtistId)
        }
    }

    // read

    @GET
    @Transactional
    fun getAll() = Answer.ok(albumService.readAll())

    @GET
    @Path("{id}")
    @Transactional
    fun get(@PathParam("id") id: Long): Response {
        val album = albumService.read(id)

        return when (album != null) {
            true -> Answer.ok(album)
            false -> Answer.badRequest(errorId)
        }
    }

    // update

    @PUT
    @Path("{id}")
    @Transactional
    fun update(@PathParam("id") id: Long, album: Album): Response {
        val albumUpdated = albumService.update(id, album)

        return when (albumUpdated != null) {
            true -> Answer.ok(albumUpdated)
            false -> Answer.badRequest(errorId)
        }
    }

    // delete

    @DELETE
    @Path("{id}")
    @Transactional
    fun delete(@PathParam("id") id: Long): Response {
        return when (albumService.delete(id)) {
            true -> Answer.noContent()
            false -> Answer.badRequest(errorId)
        }
    }
}
