package org.musiccollection.resource

import org.musiccollection.Fixtures
import org.musiccollection.domain.AlbumRequest
import org.musiccollection.domain.ArtistResponse
import org.musiccollection.service.S3Bucket
import org.musiccollection.util.asAlbum
import org.musiccollection.util.asAlbums
import org.musiccollection.util.ALBUM_ID
import org.musiccollection.util.createAlbum
import org.musiccollection.util.createArtist
import org.musiccollection.util.shouldBeAlbum
import org.musiccollection.util.shouldBeAlbums
import org.musiccollection.util.uploadAlbumImage
import io.kotest.assertions.throwables.shouldThrow
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
import software.amazon.awssdk.services.s3.model.NoSuchKeyException

@QuarkusTest
@TestSecurity(user = "user", roles = [Role.USER])
class AlbumResourceTest : BaseResourceTest() {

    private lateinit var artistCreated: ArtistResponse

    private val Fixtures.Album.issuesWithArtistId: AlbumRequest
        get() = issues.copy(artistId = artistCreated.id)

    @BeforeEach
    @Transactional
    fun beforeEach() {
        // precondition to all tests: create an artist
        artistCreated = createArtist(Fixtures.Artist.korn)
    }

    // region create

    @Test
    fun `should not create an album with invalid artist-id`() {
        Given {
            body(Fixtures.Album.issues)
        } When {
            post(Resource.Path.ALBUM)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should create an album with valid request`() {
        val albumResponse = Given {
            body(Fixtures.Album.issuesWithArtistId)
        } When {
            post(Resource.Path.ALBUM)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asAlbum()
        }

        albumResponse.shouldNotBeNull()
        albumResponse shouldBeAlbum Fixtures.Album.issuesWithArtistId
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`() {
        val albumResponse = When {
            get(Resource.Path.ALBUM)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbums()
        }

        albumResponse.shouldNotBeNull()
        albumResponse shouldBe emptyList()
    }

    @Test
    fun `should return a list with one album`() {
        createAlbum(Fixtures.Album.issuesWithArtistId)

        val albums = When {
            get(Resource.Path.ALBUM)
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

        val albumResponse = When {
            get(Resource.Path.ALBUM)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbums()
        }

        albumResponse shouldHaveSize 10
        albumResponse shouldBeAlbums albumsRequest
    }

    // endregion

    // region read one

    @Test
    fun `should throw bad-request with invalid id`() {
        createAlbum(Fixtures.Album.issuesWithArtistId)

        When {
            get(Resource.Path.ALBUM_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should return one album with valid id`() {
        val albumCreated = createAlbum(Fixtures.Album.issuesWithArtistId)

        val albumResponse = When {
            get(Resource.Path.ALBUM_ID, albumCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbum()
        }

        albumResponse.shouldNotBeNull()
        albumResponse shouldBeAlbum Fixtures.Album.issuesWithArtistId
    }

    // endregion

    // region update

    @Test
    fun `should not update an album and throw bad-request with invalid id`() {
        createAlbum(Fixtures.Album.issuesWithArtistId)

        Given {
            body(Fixtures.Album.untouchables.copy(artistId = artistCreated.id))
        } When {
            put(Resource.Path.ALBUM_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not update an album and throw bad-request with invalid artist-id`() {
        val albumCreated = createAlbum(Fixtures.Album.issuesWithArtistId)

        Given {
            body(Fixtures.Album.untouchables.copy(artistId = 100))
        } When {
            put(Resource.Path.ALBUM_ID, albumCreated.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should update and return an album with valid id`() {
        val albumCreated = createAlbum(Fixtures.Album.issuesWithArtistId)
        val albumUpdate = Fixtures.Album.untouchables.copy(artistId = artistCreated.id)

        val albumResponse = Given {
            body(albumUpdate)
        } When {
            put(Resource.Path.ALBUM_ID, albumCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asAlbum()
        }

        albumResponse.shouldNotBeNull()
        albumResponse shouldBeAlbum albumUpdate
    }

    // endregion

    // region delete

    @Test
    fun `should not delete all albums with user-role`() {
        // precondition: create an album
        createAlbum(Fixtures.Album.issuesWithArtistId)

        // delete all albums
        When {
            delete(Resource.Path.ALBUM)
        } Then {
            statusCode(HttpStatus.SC_FORBIDDEN)
        }
    }

    @Test
    @TestSecurity(user = "user", roles = [Role.ADMIN, Role.USER])
    fun `should delete all albums with admin-role`() {
        // precondition: create an album
        createAlbum(Fixtures.Album.issuesWithArtistId)

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
        createAlbum(Fixtures.Album.issuesWithArtistId)

        When {
            delete(Resource.Path.ALBUM_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete an album with valid id`() {
        val albumCreated = createAlbum(Fixtures.Album.issuesWithArtistId)

        When {
            delete(Resource.Path.ALBUM_ID, albumCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    @Test
    fun `should delete an album and an image with valid id`() {
        val albumCreated = uploadAlbumImage(createAlbum(Fixtures.Album.issuesWithArtistId).id)

        When {
            delete(Resource.Path.ALBUM_ID, albumCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }

        // try to download the image and verify, that it is deleted
        shouldThrow<NoSuchKeyException> {
            s3Service.downloadFile(S3Bucket.ALBUM, albumCreated.filename!!)
        }
    }

    // endregion
}
