package com.alex.musicfreak.controller

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.domain.model.Artist
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
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

open class BaseControllerTest {

    companion object {
        @Container
        private val postgres = PostgreSQLContainer<Nothing>("postgres:17.4").apply {
            withDatabaseName("testDb")
            withUsername("test")
            withPassword("test")
        }

        @JvmStatic
        @BeforeAll
        fun beforeAllBase() {
            postgres.start()

            System.setProperty("quarkus.datasource.jdbc.url", postgres.jdbcUrl)
            System.setProperty("quarkus.datasource.username", postgres.username)
            System.setProperty("quarkus.datasource.password", postgres.password)
        }
    }

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
}
