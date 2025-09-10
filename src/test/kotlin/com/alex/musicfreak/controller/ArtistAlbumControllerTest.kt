package com.alex.musicfreak.controller

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.domain.ArtistResponse
import com.alex.musicfreak.util.asAlbums
import com.alex.musicfreak.util.shouldBeAlbums
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
@TestSecurity(user = "user", roles = [Role.USER])
class ArtistAlbumControllerTest : BaseControllerTest() {

    private lateinit var artistPosted: ArtistResponse
    private lateinit var albumWithArtistId: Album

    @BeforeEach
    @Transactional
    fun beforeEach() {
        // precondition to all tests: post an artist
        artistPosted = postArtist(Fixtures.Artist.korn)
        albumWithArtistId = Fixtures.Album.Domain.issues.copy(artistId = artistPosted.id)
    }

    // region read all

    @Test
    fun `should return bad-request due invalid artist-id`() {
        When {
            get(Resource.Path.ARTIST_ALBUM, 10)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should return an empty list`() {
        val albums = When {
            get(Resource.Path.ARTIST_ALBUM, artistPosted.id)
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
            get(Resource.Path.ARTIST_ALBUM, artistPosted.id)
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
            get(Resource.Path.ARTIST_ALBUM, artistPosted.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbums()
        }

        albums shouldHaveSize 10
        albums shouldBeAlbums albumsRequest
    }

    //endregion
}
