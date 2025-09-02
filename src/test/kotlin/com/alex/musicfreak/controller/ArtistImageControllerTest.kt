package com.alex.musicfreak.controller

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.Artist
import com.alex.musicfreak.extension.asArtist
import com.alex.musicfreak.testresource.MinioTestResource
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
class ArtistImageControllerTest : BaseControllerTest() {

    private lateinit var artistPosted: Artist

    @BeforeEach
    @Transactional
    fun beforeEach() {
        // precondition to all tests: post an artist
        artistPosted = postArtist(Fixtures.Artist.Domain.korn)
    }

    // region upload image

    @Test
    fun `should throw bad-request with invalid id`() {
        Given {
            multiPart("file", image)
            contentType(ContentType.MULTIPART)
        } When {
            post(Resource.Path.ARTIST_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should upload image and return ok with valid id`() {
        val artist = Given {
            multiPart("image", image)
            contentType(ContentType.MULTIPART)
        } When {
            post(Resource.Path.ARTIST_IMAGE, artistPosted.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asArtist()
        }

        artist.shouldNotBeNull()
        artist.filename.shouldNotBeNull()
        artist.filename.shouldNotBeBlank()
    }

    // endregion

    // region download image

    @Test
    fun `should not download an image and throw bad-request with invalid id`() {
        // precondition: upload an image
        uploadArtistImage(artistPosted.id)

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
        uploadArtistImage(artistPosted.id)

        val bytes = When {
            get(Resource.Path.ARTIST_IMAGE, artistPosted.id)
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
        uploadArtistImage(artistPosted.id)

        When {
            delete(Resource.Path.ARTIST_IMAGE, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should not delete an image and with non existing image`() {
        When {
            delete(Resource.Path.ARTIST_IMAGE, artistPosted.id)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete an image and with valid id and existing image`() {
        // precondition: upload an image
        uploadArtistImage(artistPosted.id)

        When {
            delete(Resource.Path.ARTIST_IMAGE, artistPosted.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion
}
