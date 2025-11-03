package com.alex.musicfreak.resource

import com.alex.musicfreak.domain.AlbumRequest
import com.alex.musicfreak.service.AlbumService
import com.alex.musicfreak.util.convertToSort
import io.smallrye.faulttolerance.api.RateLimit
import jakarta.annotation.security.RolesAllowed
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

@Suppress("unused")
@Path(Resource.Path.ALBUM)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(Role.USER)
@RateLimit
class AlbumResource(private val albumService: AlbumService) {

    // create

    @POST
    fun create(album: AlbumRequest) = Answer.created(albumService.create(album))

    // read

    @GET
    fun readAll(@QueryParam(Resource.Param.SORT) sort: String?) = albumService.readAll(sort.convertToSort())

    @GET
    @Path(Resource.Path.ID)
    fun read(@PathParam(Resource.Param.ID) id: Long) = albumService.read(id)

    // update

    @PUT
    @Path(Resource.Path.ID)
    fun update(@PathParam(Resource.Param.ID) id: Long, album: AlbumRequest) = albumService.update(id, album)

    // delete

    @DELETE
    @RolesAllowed(Role.ADMIN)
    fun deleteAll() = albumService.deleteAll()

    @DELETE
    @Path(Resource.Path.ID)
    fun delete(@PathParam(Resource.Param.ID) id: Long) = albumService.delete(id)
}
