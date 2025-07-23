package com.alex.musicfreak.controller

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.domain.Artist
import com.alex.musicfreak.extension.asAlbum
import com.alex.musicfreak.extension.asAlbums
import com.alex.musicfreak.extension.asArtist
import com.alex.musicfreak.repository.album.AlbumRepository
import com.alex.musicfreak.repository.artist.ArtistRepository
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class AlbumControllerTest : BaseControllerTest() {

    private lateinit var artist: Artist

    @Inject
    private lateinit var artistRepository: ArtistRepository

    @Inject
    private lateinit var albumRepository: AlbumRepository

    @BeforeEach
    @Transactional
    fun beforeEach() {
        artist = postArtist(Fixtures.Artists.korn)
    }

    @AfterEach
    @Transactional
    fun afterEach() {
        artistRepository.deleteAll()
        albumRepository.deleteAll()
    }

    // region create

    @Test
    fun `should not create an album with invalid artist-id`() {
        Given {
            body(Fixtures.Album.issues.copy(artistId = 10))
        } When {
            post(Routes.Album.MAIN)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should create an album with valid request`() {
        val album = Given {
            body(Fixtures.Album.issues.copy(artistId = artist.id))
        } When {
            post(Routes.Album.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asAlbum()
        }

        album.shouldNotBeNull()
        album shouldBeAlbum Fixtures.Album.issues.copy(artistId = artist.id)
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`() {
        val albums = When {
            get(Routes.Album.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbums()
        }

        albums.shouldNotBeNull()
        albums shouldBe emptyList()
    }

    @Test
    fun `should return a list with one album`() {
        postAlbum(Fixtures.Album.issues.copy(artistId = artist.id))

        val albums = When {
            get(Routes.Album.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbums()
        }

        albums shouldHaveSize 1
        albums shouldBeAlbums listOf(Fixtures.Album.issues.copy(artistId = artist.id))
    }

    @Test
    fun `should return a list with ten albums`() {
        val albumsRequest = (1..10).map { Fixtures.Album.issues.copy(artistId = artist.id) }

        albumsRequest.forEach { postAlbum(it) }

        val albums = When {
            get(Routes.Album.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbums()
        }

        albums shouldHaveSize 10
        albums shouldBeAlbums albumsRequest
    }

    // endregion

    private fun postArtist(artist: Artist): Artist {
        return Given {
            body(artist)
        } When {
            post(Routes.Artist.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asArtist()
        }
    }

    private fun postAlbum(album: Album): Album {
        return Given {
            body(album)
        } When {
            post(Routes.Album.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asAlbum()
        }
    }

    private infix fun List<Album>.shouldBeAlbums(expected: List<Album>) {
        zip(expected).forEach { (albumActual, albumExpected) ->
            albumActual shouldBeAlbum albumExpected
        }
    }

    private infix fun Album.shouldBeAlbum(expected: Album) {
        id.shouldNotBeNull()
        id shouldBeGreaterThan 0
        artistId.shouldNotBeNull()
        artistId shouldBe expected.artistId
        name.shouldNotBeNull()
        name shouldBe expected.name
        year.shouldNotBeNull()
        year shouldBe expected.year
        tracks.shouldNotBeNull()
        tracks shouldBe expected.tracks
        createdAt.shouldNotBeNull()
        updatedAt.shouldNotBeNull()
    }
}