package com.alex.musicfreak.controller

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.domain.Artist
import com.alex.musicfreak.extension.asAlbum
import com.alex.musicfreak.extension.asAlbums
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

    private lateinit var artistPosted: Artist
    private lateinit var albumWithArtistId: Album

    @Inject
    private lateinit var artistRepository: ArtistRepository

    @Inject
    private lateinit var albumRepository: AlbumRepository

    @BeforeEach
    @Transactional
    fun beforeEach() {
        artistPosted = postArtist(Fixtures.Artists.korn)
        albumWithArtistId = Fixtures.Album.issues.copy(artistId = artistPosted.id)
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
            body(albumWithArtistId)
        } When {
            post(Routes.Album.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asAlbum()
        }

        album.shouldNotBeNull()
        album shouldBeAlbum albumWithArtistId
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
        postAlbum(albumWithArtistId)

        val albums = When {
            get(Routes.Album.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbums()
        }

        albums shouldHaveSize 1
        albums shouldBeAlbums listOf(albumWithArtistId)
    }

    @Test
    fun `should return a list with ten albums`() {
        val albumsRequest = (1..10).map { albumWithArtistId }

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

    // region read one

    @Test
    fun `should throw bad-request with invalid id`() {
        postAlbum(albumWithArtistId)

        When {
            get(Routes.Album.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should return one album with valid id`() {
        val albumPosted = postAlbum(albumWithArtistId)

        val album = When {
            get(Routes.Album.DETAIL, albumPosted.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbum()
        }

        album.shouldNotBeNull()
        album shouldBeAlbum albumWithArtistId
    }

    // endregion

    // region update

    @Test
    fun `should not update an album and throw bad-request with invalid id`() {
        postAlbum(albumWithArtistId)

        Given {
            body(Fixtures.Album.untouchables.copy(artistId = artistPosted.id))
        } When {
            put(Routes.Album.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not update an album and throw bad-request with invalid artist-id`() {
        val albumPosted = postAlbum(albumWithArtistId)

        Given {
            body(Fixtures.Album.untouchables.copy(artistId = 100))
        } When {
            put(Routes.Album.DETAIL, albumPosted.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should update and return an album with valid id`() {
        val albumPosted = postAlbum(albumWithArtistId)
        val albumUpdate = Fixtures.Album.untouchables.copy(artistId = artistPosted.id)

        val album = Given {
            body(albumUpdate)
        } When {
            put(Routes.Album.DETAIL, albumPosted.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbum()
        }

        album.shouldNotBeNull()
        album shouldBeAlbum albumUpdate
    }

    // endregion

    // region delete

    @Test
    fun `should not delete an album and throw bad-request with invalid id`() {
        postAlbum(albumWithArtistId)

        When {
            delete(Routes.Album.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete an album with valid id`() {
        val albumPosted = postAlbum(albumWithArtistId)

        When {
            delete(Routes.Album.DETAIL, albumPosted.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion

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
