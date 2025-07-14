package com.alex.musicfreak.controller

import com.alex.musicfreak.repository.api.ApiModelAlbum
import com.alex.musicfreak.repository.database.album.AlbumRepository
import com.alex.musicfreak.repository.database.artist.ArtistRepository
import com.alex.musicfreak.repository.mapper.mergeDbModel
import com.alex.musicfreak.repository.mapper.newDbModel
import com.alex.musicfreak.repository.mapper.toApiModel
import com.alex.musicfreak.repository.mapper.toApiModels
import com.alex.musicfreak.util.Answer
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.PUT
import jakarta.ws.rs.Path
import jakarta.ws.rs.PathParam
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response

@Path("api/v1/albums")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class AlbumController(
    private val albumRepository: AlbumRepository,
    private val artistRepository: ArtistRepository,
    private val entityManager: EntityManager
) {

    private val errorId = "Album not found!"
    private val errorArtistId = "Artist not found!"

    // create

    @POST
    @Transactional
    fun post(album: ApiModelAlbum): Response {
        if (!artistRepository.exists(album.artistId!!)) return Answer.badRequest(errorArtistId)

        return Answer.created(albumRepository.save(album.newDbModel()).toApiModel())
    }

    // read

    @GET
    @Transactional
    fun getAll() = Answer.ok(albumRepository.listAll().toApiModels())

    @GET
    @Path("{id}")
    @Transactional
    fun get(@PathParam("id") id: Long): Response {
        return albumRepository
            .findById(id)
            ?.toApiModel()
            ?.let { Answer.ok(it) }
            ?: Answer.badRequest(errorId)
    }

    // update

    @PUT
    @Path("{id}")
    @Transactional
    fun update(@PathParam("id") id: Long, album: ApiModelAlbum): Response {
        val albumSaved = albumRepository.findById(id)

        // check if album and artist are valid
        if (albumSaved == null) return Answer.badRequest(errorId)
        if (!artistRepository.exists(album.artistId!!)) return Answer.badRequest(errorArtistId)

        return Answer.ok(entityManager.merge(album.mergeDbModel(albumSaved)))
    }

    // delete

    @DELETE
    @Path("{id}")
    @Transactional
    fun delete(@PathParam("id") id: Long): Response {
        return when (albumRepository.deleteById(id)) {
            true -> Answer.noContent()
            false -> Answer.badRequest(errorId)
        }
    }
}
