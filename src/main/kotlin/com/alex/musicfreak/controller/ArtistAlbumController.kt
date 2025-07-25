package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.ArtistAlbumService
import com.alex.musicfreak.util.Answer
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("api/v1/artists/{id}/albums")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ArtistAlbumController(private val artistAlbumService: ArtistAlbumService) {

    @GET
    fun getAllAlbumsFromArtist(@PathParam("id") id: Long, @QueryParam("sort") sort: String?): Response {
        return Answer.ok(artistAlbumService.readAll(id, sort))
    }
}
