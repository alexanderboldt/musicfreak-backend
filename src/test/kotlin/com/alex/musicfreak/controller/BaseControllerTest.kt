package com.alex.musicfreak.controller

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.AlbumRequest
import com.alex.musicfreak.domain.AlbumResponse
import com.alex.musicfreak.domain.ArtistRequest
import com.alex.musicfreak.domain.ArtistResponse
import com.alex.musicfreak.service.S3Service
import com.alex.musicfreak.util.asAlbum
import com.alex.musicfreak.util.asArtist
import com.alex.musicfreak.repository.AlbumRepository
import com.alex.musicfreak.repository.ArtistRepository
import com.alex.musicfreak.service.UserService
import io.quarkus.test.InjectMock
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
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever
import java.io.File

abstract class BaseControllerTest {

    @InjectMock
    lateinit var userService: UserService

    @Inject
    protected lateinit var s3Service: S3Service

    @Inject
    protected lateinit var artistRepository: ArtistRepository

    @Inject
    protected lateinit var albumRepository: AlbumRepository

    @BeforeEach
    @Transactional
    fun beforeEachBase() {
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)

        whenever(userService.userId).doReturn(Fixtures.User.USER_ID)
    }

    @AfterEach
    @Transactional
    fun afterEachBase() {
        artistRepository.deleteAll()
        albumRepository.deleteAll()
    }

    protected val image: File = File.createTempFile("image", ".jpg").apply {
        writeText("Image Content")
        deleteOnExit()
    }

    protected fun postAlbum(album: AlbumRequest): AlbumResponse {
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

    protected fun postArtist(artist: ArtistRequest): ArtistResponse {
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

    protected fun uploadAlbumImage(albumId: Long): AlbumResponse {
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

    protected fun uploadArtistImage(artistId: Long): ArtistResponse {
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
