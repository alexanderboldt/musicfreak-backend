package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.service.ArtistAlbumService
import com.alex.musicfreak.util.Resource
import com.alex.musicfreak.util.Role
import com.alex.musicfreak.util.convertToSort
import jakarta.annotation.security.RolesAllowed
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType

@Path(Resource.Path.ARTIST_ALBUM)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(Role.USER)
class ArtistAlbumController(private val artistAlbumService: ArtistAlbumService) {

    @GET
    fun getAllAlbumsFromArtist(
        @PathParam(Resource.Param.ID) artistId: Long,
        @QueryParam(Resource.Param.SORT) sort: String?
    ): List<Album> {
        return artistAlbumService.readAll(artistId, sort.convertToSort())
    }
}
