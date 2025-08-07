package com.alex.musicfreak.controller

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.extension.asArtist
import com.alex.musicfreak.extension.asArtists
import com.alex.musicfreak.repository.artist.ArtistRepository
import com.alex.musicfreak.util.Resource
import com.alex.musicfreak.util.shouldBeArtist
import com.alex.musicfreak.util.shouldBeArtists
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.security.TestSecurity
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

@QuarkusTest
@TestSecurity(user = "test-user", roles = ["user"])
class ArtistControllerTest : BaseControllerTest() {

    @Inject
    private lateinit var artistRepository: ArtistRepository

    @AfterEach
    @Transactional
    fun afterEach() {
        artistRepository.deleteAll()
    }

    // region create

    @Test
    fun `should create an artist with valid request`() {
        // execute and verify
        val artist = Given {
            body(Fixtures.Artist.Domain.korn)
        } When {
            post(Resource.Path.ARTIST)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            asArtist()
        }

        artist.shouldNotBeNull()
        artist shouldBeArtist Fixtures.Artist.Domain.korn
    }

    // endregion

    // region read all

    @Test
    fun `should return an empty list`() {
        // execute and verify
        val artists = When {
            get(Resource.Path.ARTIST)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(0))
        } Extract {
            asArtists()
        }

        artists.shouldNotBeNull()
        artists shouldBe emptyList()
    }

    @Test
    fun `should return a list with one artist`() {
        // precondition: post an artist
        postArtist(Fixtures.Artist.Domain.korn)

        // execute and verify
        val artists = When {
            get(Resource.Path.ARTIST)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asArtists()
        }

        artists.shouldNotBeNull()
        artists shouldBeArtists listOf(Fixtures.Artist.Domain.korn)
    }

    // endregion

    // region read one

    @Test
    fun `should throw bad-request with invalid id`() {
        // precondition: post an artist
        postArtist(Fixtures.Artist.Domain.korn)

        // execute and verify
        When {
            get(Resource.Path.ARTIST_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should return one artist with valid id`() {
        // precondition: post an artist
        val artistPosted = postArtist(Fixtures.Artist.Domain.korn)

        // execute and verify
        val artist = When {
            get(Resource.Path.ARTIST_ID, artistPosted.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asArtist()
        }

        artist.shouldNotBeNull()
        artist shouldBeArtist Fixtures.Artist.Domain.korn
    }

    // endregion

    // region update

    @Test
    fun `should not update an artist and throw bad-request with invalid id`() {
        // precondition: post an artist
        postArtist(Fixtures.Artist.Domain.korn)

        // execute the update and verify
        Given {
            body(Fixtures.Artist.Domain.slipknot)
        } When {
            put(Resource.Path.ARTIST_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should update and return an artist with valid id`() {
        // precondition: post an artist
        val artistPosted = postArtist(Fixtures.Artist.Domain.korn)

        // execute the update and verify
        val artist = Given {
            body(Fixtures.Artist.Domain.slipknot)
        } When {
            put(Resource.Path.ARTIST_ID, artistPosted.id)
        } Then {
            statusCode(HttpStatus.SC_OK)
        } Extract {
            asArtist()
        }

        artist.shouldNotBeNull()
        artist shouldBeArtist Fixtures.Artist.Domain.slipknot
    }

    // endregion

    // region delete

    @Test
    fun `should not delete an artist and throw bad-request with invalid id`() {
        // precondition: post an artist
        postArtist(Fixtures.Artist.Domain.korn)

        // execute the delete and verify
        When {
            delete(Resource.Path.ARTIST_ID, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun `should delete an artist with valid id`() {
        // precondition: post an artist
        val artistPosted = postArtist(Fixtures.Artist.Domain.korn)

        // execute the delete and verify
        When {
            delete(Resource.Path.ARTIST_ID, artistPosted.id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion
}
