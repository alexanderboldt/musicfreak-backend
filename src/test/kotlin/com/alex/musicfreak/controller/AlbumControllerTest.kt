package com.alex.musicfreak.controller

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.extension.asAlbum
import com.alex.musicfreak.extension.asAlbums
import com.alex.musicfreak.util.Resource
import com.alex.musicfreak.util.Role
import com.alex.musicfreak.util.shouldBeAlbum
import com.alex.musicfreak.util.shouldBeAlbums
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
@TestSecurity(user = "test-user", roles = ["user"])
class AlbumControllerTest : BaseControllerTest() {

    private lateinit var artistPosted: Artist
    private lateinit var albumWithArtistId: Album

    private val Resource.Path.ALBUM_ID: String
        get() = "${Resource.Path.ALBUM}/${Resource.Path.ID}"

    @BeforeEach
    @Transactional
    fun beforeEach() {
        artistPosted = postArtist(Fixtures.Artist.Domain.korn)
        albumWithArtistId = Fixtures.Album.Domain.issues.copy(artistId = artistPosted.id!!)
    }

    // region create

    @Test
    fun `should not create an album with invalid artist-id`() {
        Given {
            body(Fixtures.Album.Domain.issues)
        } When {
            post(Resource.Path.ALBUM)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should create an album with valid request`() {
        val album = Given {
            body(albumWithArtistId)
        } When {
            post(Resource.Path.ALBUM)
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
            get(Resource.Path.ALBUM)
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
            get(Resource.Path.ALBUM)
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
            get(Resource.Path.ALBUM)
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
            get(Resource.Path.ALBUM_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should return one album with valid id`() {
        val albumPosted = postAlbum(albumWithArtistId)

        val album = When {
            get(Resource.Path.ALBUM_ID, albumPosted.id)
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
            body(Fixtures.Album.Domain.untouchables.copy(artistId = artistPosted.id!!))
        } When {
            put(Resource.Path.ALBUM_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not update an album and throw bad-request with invalid artist-id`() {
        val albumPosted = postAlbum(albumWithArtistId)

        Given {
            body(Fixtures.Album.Domain.untouchables.copy(artistId = 100))
        } When {
            put(Resource.Path.ALBUM_ID, albumPosted.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should update and return an album with valid id`() {
        val albumPosted = postAlbum(albumWithArtistId)
        val albumUpdate = Fixtures.Album.Domain.untouchables.copy(artistId = artistPosted.id!!)

        val album = Given {
            body(albumUpdate)
        } When {
            put(Resource.Path.ALBUM_ID, albumPosted.id)
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
    fun `should not delete all albums with user-role`() {
        // precondition: post an album
        postAlbum(albumWithArtistId)

        // delete all albums
        When {
            delete(Resource.Path.ALBUM)
        } Then {
            statusCode(HttpStatus.SC_FORBIDDEN)
        }
    }

    @Test
    @TestSecurity(user = "test-user", roles = [Role.ADMIN])
    fun `should delete all albums with admin-role`() {
        // precondition: post an album
        postAlbum(albumWithArtistId)

        // delete all albums
        When {
            delete(Resource.Path.ALBUM)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }

        // verify that all albums are deleted
        val albums = When {
            get(Resource.Path.ALBUM)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbums()
        }

        albums.shouldNotBeNull()
        albums shouldBe emptyList()
    }

    @Test
    fun `should not delete an album and throw bad-request with invalid id`() {
        postAlbum(albumWithArtistId)

        When {
            delete(Resource.Path.ALBUM_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete an album with valid id`() {
        val albumPosted = postAlbum(albumWithArtistId)

        When {
            delete(Resource.Path.ALBUM_ID, albumPosted.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion
}
