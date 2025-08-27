package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.domain.service.S3Service
import com.alex.musicfreak.extension.asAlbum
import com.alex.musicfreak.extension.asArtist
import com.alex.musicfreak.repository.album.AlbumRepository
import com.alex.musicfreak.repository.artist.ArtistRepository
import com.alex.musicfreak.util.Resource
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import java.io.File

abstract class BaseControllerTest {

    @BeforeEach
    @Transactional
    fun beforeEachBase() {
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)
    }

    @AfterEach
    @Transactional
    fun afterEachBase() {
        artistRepository.deleteAll()
        albumRepository.deleteAll()
    }

    @Inject
    protected lateinit var artistRepository: ArtistRepository

    @Inject
    protected lateinit var albumRepository: AlbumRepository

    @Inject
    protected lateinit var s3Service: S3Service

    protected val image: File = File.createTempFile("image", ".jpg").apply {
        writeText("Image Content")
        deleteOnExit()
    }

    protected fun postAlbum(album: Album): Album {
        return Given {
            body(album)
        } When {
            post(Resource.Path.ALBUM)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asAlbum()
        }
    }

    protected fun postArtist(artist: Artist): Artist {
        return Given {
            body(artist)
        } When {
            post(Resource.Path.ARTIST)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asArtist()
        }
    }

    protected fun uploadAlbumImage(albumId: Long): Album {
        return Given {
            multiPart("image", image)
            contentType(ContentType.MULTIPART)
        } When {
            post(Resource.Path.ALBUM_IMAGE, albumId)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbum()
        }
    }

    protected fun uploadArtistImage(artistId: Long): Artist {
        return Given {
            multiPart("image", image)
            contentType(ContentType.MULTIPART)
        } When {
            post(Resource.Path.ARTIST_IMAGE, artistId)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asArtist()
        }
    }
}
