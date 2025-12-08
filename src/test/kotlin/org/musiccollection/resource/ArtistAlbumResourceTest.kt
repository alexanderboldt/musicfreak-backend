package org.musiccollection.resource

import org.musiccollection.Fixtures
import org.musiccollection.domain.AlbumRequest
import org.musiccollection.domain.ArtistResponse
import org.musiccollection.util.asAlbums
import org.musiccollection.util.createAlbum
import org.musiccollection.util.createArtist
import org.musiccollection.util.shouldBeAlbums
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
class ArtistAlbumResourceTest : BaseResourceTest() {

    private lateinit var artistCreated: ArtistResponse

    private val Fixtures.Album.issuesWithArtistId: AlbumRequest
        get() = issues.copy(artistId = artistCreated.id)

    @BeforeEach
    @Transactional
    fun beforeEach() {
        // precondition to all tests: create an artist
        artistCreated = createArtist(Fixtures.Artist.korn)
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
            get(Resource.Path.ARTIST_ALBUM, artistCreated.id)
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
        createAlbum(Fixtures.Album.issuesWithArtistId)

        val albums = When {
            get(Resource.Path.ARTIST_ALBUM, artistCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbums()
        }

        albums shouldHaveSize 1
        albums shouldBeAlbums listOf(Fixtures.Album.issuesWithArtistId)
    }

    @Test
    fun `should return a list with ten albums`() {
        val albumsRequest = (1..10).map { Fixtures.Album.issuesWithArtistId }

        albumsRequest.forEach { createAlbum(it) }

        val albums = When {
            get(Resource.Path.ARTIST_ALBUM, artistCreated.id)
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
