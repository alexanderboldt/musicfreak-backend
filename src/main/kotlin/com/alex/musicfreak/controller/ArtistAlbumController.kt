package com.alex.musicfreak.controller

import com.alex.musicfreak.mapper.toDomain
import com.alex.musicfreak.repository.album.AlbumRepository
import com.alex.musicfreak.util.Answer
import io.quarkus.panache.common.Sort
import jakarta.transaction.Transactional
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
class ArtistAlbumController(private val albumRepository: AlbumRepository) {

    @GET
    @Transactional
    fun getAllAlbumsFromArtist(@PathParam("id") id: Long, @QueryParam("sort") sort: String?): Response {
        return Answer.ok(albumRepository.list("artistId", if (sort != null) Sort.by(sort) else Sort.by("year"), id).map { it.toDomain() })
    }
}