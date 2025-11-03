package com.alex.musicfreak.resource

import com.alex.musicfreak.domain.AlbumResponse
import com.alex.musicfreak.service.ArtistAlbumService
import com.alex.musicfreak.util.convertToSort
import io.smallrye.faulttolerance.api.RateLimit
import jakarta.annotation.security.RolesAllowed
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.QueryParam
import jakarta.ws.rs.core.MediaType

@Suppress("unused")
@Path(Resource.Path.ARTIST_ALBUM)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(Role.USER)
@RateLimit
class ArtistAlbumResource(private val artistAlbumService: ArtistAlbumService) {

    @GET
    fun readAllAlbumsFromArtist(
        @PathParam(Resource.Param.ID) artistId: Long,
        @QueryParam(Resource.Param.SORT) sort: String?
    ): List<AlbumResponse> {
        return artistAlbumService.readAll(artistId, sort.convertToSort())
    }
}
