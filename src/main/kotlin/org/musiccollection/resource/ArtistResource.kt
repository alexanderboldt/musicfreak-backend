package org.musiccollection.resource

import org.musiccollection.domain.ArtistRequest
import org.musiccollection.service.ArtistService
import org.musiccollection.util.convertToSort
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
@Path(Resource.Path.ARTIST)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(Role.USER)
@RateLimit
class ArtistResource(private val artistService: ArtistService) {

    // create

    @POST
    fun create(artist: ArtistRequest) = Answer.created(artistService.create(artist))

    // read

    @GET
    fun readAll(@QueryParam(Resource.Param.SORT) sort: String?) = artistService.readAll(sort.convertToSort())

    @GET
    @Path(Resource.Path.ID)
    fun read(@PathParam(Resource.Param.ID) id: Long) = artistService.read(id)

    // update

    @PUT
    @Path(Resource.Path.ID)
    fun update(@PathParam(Resource.Param.ID) id: Long, artist: ArtistRequest) = artistService.update(id, artist)

    // delete

    @DELETE
    @Path(Resource.Path.ID)
    fun delete(@PathParam(Resource.Param.ID) id: Long) = artistService.delete(id)
}
