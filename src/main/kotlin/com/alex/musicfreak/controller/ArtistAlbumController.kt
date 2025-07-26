package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.service.ArtistAlbumService
import com.alex.musicfreak.util.Answer
import com.alex.musicfreak.util.Resource
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path(Resource.Path.ARTISTS_ALBUMS)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ArtistAlbumController(private val artistAlbumService: ArtistAlbumService) {

    @GET
    fun getAllAlbumsFromArtist(@PathParam(Resource.Param.ID) id: Long, @QueryParam(Resource.Param.SORT) sort: String?): Response {
        val albums = artistAlbumService.readAll(id, sort)

        return when (albums != null) {
            true -> Answer.ok(albums)
            false -> Answer.badRequest()
        }
    }
}
