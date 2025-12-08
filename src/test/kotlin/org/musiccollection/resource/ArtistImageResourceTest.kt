package org.musiccollection.resource

import org.musiccollection.Fixtures
import org.musiccollection.domain.ArtistResponse
import org.musiccollection.util.asArtist
import org.musiccollection.testresource.MinioTestResource
import org.musiccollection.util.createArtist
import org.musiccollection.util.uploadArtistImage
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.string.shouldNotBeBlank
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
@QuarkusTestResource(MinioTestResource::class)
@TestSecurity(user = "user", roles = [Role.USER])
class ArtistImageResourceTest : BaseResourceTest() {

    private lateinit var artistCreated: ArtistResponse

    @BeforeEach
    @Transactional
    fun beforeEach() {
        // precondition to all tests: create an artist
        artistCreated = createArtist(Fixtures.Artist.korn)
    }

    // region upload image

    @Test
    fun `should throw bad-request with invalid id`() {
        Given {
            multiPart("image", Fixtures.image)
            contentType(ContentType.MULTIPART)
        } When {
            post(Resource.Path.ARTIST_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should upload image and return ok with valid id`() {
        val artistResponse = Given {
            multiPart("image", Fixtures.image)
            contentType(ContentType.MULTIPART)
        } When {
            post(Resource.Path.ARTIST_IMAGE, artistCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asArtist()
        }

        artistResponse.shouldNotBeNull()
        artistResponse.filename.shouldNotBeNull()
        artistResponse.filename.shouldNotBeBlank()
    }

    // endregion

    // region download image

    @Test
    fun `should not download an image and throw bad-request with invalid id`() {
        // precondition: upload an image
        uploadArtistImage(artistCreated.id)

        When {
            get(Resource.Path.ARTIST_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
            contentType(ContentType.BINARY)
        }
    }

    @Test
    fun `should download an image and with valid id`() {
        // precondition: upload an image
        uploadArtistImage(artistCreated.id)

        val bytes = When {
            get(Resource.Path.ARTIST_IMAGE, artistCreated.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            contentType(ContentType.BINARY)
        } Extract {
            asByteArray()
        }

        bytes.shouldNotBeNull()
        bytes.size shouldBeGreaterThan 0
    }

    // endregion

    // region delete image

    @Test
    fun `should not delete an image and throw bad-request with invalid id`() {
        // precondition: upload an image
        uploadArtistImage(artistCreated.id)

        When {
            delete(Resource.Path.ARTIST_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not delete an image and with non existing image`() {
        When {
            delete(Resource.Path.ARTIST_IMAGE, artistCreated.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete an image and with valid id and existing image`() {
        // precondition: upload an image
        uploadArtistImage(artistCreated.id)

        When {
            delete(Resource.Path.ARTIST_IMAGE, artistCreated.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion
}
