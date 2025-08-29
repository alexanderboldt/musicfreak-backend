package com.alex.musicfreak.controller

import com.alex.musicfreak.service.ArtistImageService
import com.alex.musicfreak.util.Answer
import com.alex.musicfreak.util.Resource
import com.alex.musicfreak.util.Role
import jakarta.annotation.security.RolesAllowed
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import org.jboss.resteasy.reactive.RestForm
import org.jboss.resteasy.reactive.multipart.FileUpload

@Path(Resource.Path.ARTIST_IMAGE)
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed(Role.USER)
class ArtistImageController(private val artistImageService: ArtistImageService) {

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun uploadImage(
        @PathParam(Resource.Param.ID) id: Long,
        @RestForm image: FileUpload?
    ) = artistImageService.uploadImage(id, image)

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    fun downloadImage(@PathParam(Resource.Param.ID) id: Long): Response {
        val (file, filename) = artistImageService.downloadImage(id)
        return Answer.file(file, filename)
    }

    @DELETE
    fun deleteImage(@PathParam(Resource.Param.ID) id: Long) = artistImageService.deleteImage(id)
}
