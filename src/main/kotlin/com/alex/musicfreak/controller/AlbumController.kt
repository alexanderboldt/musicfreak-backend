package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.domain.service.AlbumService
import com.alex.musicfreak.util.Answer
import com.alex.musicfreak.util.Resource
import com.alex.musicfreak.util.Role
import com.alex.musicfreak.util.convertToSort
import io.quarkus.security.Authenticated
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

@Path(Resource.Path.ALBUM)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Authenticated
class AlbumController(private val albumService: AlbumService) {

    // create

    @POST
    fun post(album: Album) = Answer.created(albumService.create(album))

    // read

    @GET
    fun getAll(@QueryParam(Resource.Param.SORT) sort: String?) = albumService.readAll(sort.convertToSort())

    @GET
    @Path(Resource.Path.ID)
    fun get(@PathParam(Resource.Param.ID) id: Long) = albumService.read(id)

    // update

    @PUT
    @Path(Resource.Path.ID)
    fun update(@PathParam(Resource.Param.ID) id: Long, album: Album) = albumService.update(id, album)

    // delete

    @DELETE
    @RolesAllowed(Role.ADMIN)
    fun deleteAll() = albumService.deleteAll()

    @DELETE
    @Path(Resource.Path.ID)
    fun delete(@PathParam(Resource.Param.ID) id: Long) = albumService.delete(id)
}
