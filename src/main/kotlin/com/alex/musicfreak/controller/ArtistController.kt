package com.alex.musicfreak.controller

import com.alex.musicfreak.repository.api.ApiModelArtistPost
import com.alex.musicfreak.repository.database.ArtistRepository
import com.alex.musicfreak.repository.mapping.toApiModelGet
import com.alex.musicfreak.repository.mapping.toDbModel
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("api/v1/artists")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class ArtistController(private val artistRepository: ArtistRepository) {

    // create

    @POST
    @Transactional
    fun postArtist(artist: ApiModelArtistPost): Response {
        artistRepository.persist(artist.toDbModel())
        return Response.status(Response.Status.CREATED).entity(artist).build()
    }

    // read

    @GET
    @Transactional
    fun getAllArtists() = artistRepository.listAll().toApiModelGet()
}