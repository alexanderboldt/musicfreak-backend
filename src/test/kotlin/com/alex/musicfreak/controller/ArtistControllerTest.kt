package com.alex.musicfreak.controller

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.Artist
import com.alex.musicfreak.repository.artist.ArtistRepository
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ValidatableResponse
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@QuarkusTest
class ArtistControllerTest {

    @Inject
    private lateinit var artistRepository: ArtistRepository

    @BeforeEach
    @Transactional
    fun beforeEach() {
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)
    }

    @AfterEach
    @Transactional
    fun afterEach() {
        artistRepository.deleteAll()
    }

    // region create

    @Test
    fun testPostWithValidRequest() {
        // execute and verify
        Given {
            body(Fixtures.Artists.korn)
        } When {
            post(Routes.Artist.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
            assertArtist(Fixtures.Artists.korn)
        }
    }

    // endregion

    // region read all

    @Test
    fun testGetAllWithNoArtists() {
        // execute and verify
        When {
            get(Routes.Artist.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(0))
        }
    }

    @Test
    fun testGetAllWithOneArtist() {
        // precondition: post an artist
        postArtist(Fixtures.Artists.korn)

        // execute and verify
        When {
            get(Routes.Artist.MAIN)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(1))
            assertArtistInArray(Fixtures.Artists.korn)
        }
    }

    // endregion

    // region read one

    @Test
    fun testGetOneWithInvalidId() {
        // precondition: post an artist
        postArtist(Fixtures.Artists.korn)

        // execute and verify
        When {
            get(Routes.Artist.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testGetOneWithValidId() {
        // precondition: post an artist
        val id = postArtist(Fixtures.Artists.korn)

        // execute and verify
        When {
            get(Routes.Artist.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertArtist(Fixtures.Artists.korn)
        }
    }

    // endregion

    // region update

    @Test
    fun testUpdateWithInvalidId() {
        // precondition: post an artist
        postArtist(Fixtures.Artists.korn)

        // execute the update and verify
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Artists.slipknot)
        } When {
            put(Routes.Artist.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testUpdateWithValidId() {
        // precondition: post an artist
        val id = postArtist(Fixtures.Artists.korn)

        // execute the update and verify
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Fixtures.Artists.slipknot)
        } When {
            put(Routes.Artist.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertArtist(Fixtures.Artists.slipknot)
        }
    }

    // endregion

    // region delete

    @Test
    fun testDeleteWithInvalidId() {
        // precondition: post an artist
        postArtist(Fixtures.Artists.korn)

        // execute the delete and verify
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
        } When {
            delete(Routes.Artist.DETAIL, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testDeleteWithValidRecipe() {
        // precondition: post an artist
        val id = postArtist(Fixtures.Artists.korn)

        // execute the delete and verify
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
        } When {
            delete(Routes.Artist.DETAIL, id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion

    private fun postArtist(artist: Artist): Int {
        return Given {
            body(artist)
        } When {
            post(Routes.Artist.MAIN)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }
    }

    private fun ValidatableResponse.assertArtist(artist: Artist) {
        body("id", Matchers.greaterThan(0))
        body("name", equalTo(artist.name))
        body("createdAt", Matchers.greaterThan(0L))
        body("updatedAt", Matchers.greaterThan(0L))
    }

    private fun ValidatableResponse.assertArtistInArray(artist: Artist) {
        body("id[0]", Matchers.greaterThan(0))
        body("name[0]", equalTo(artist.name))
        body("createdAt[0]", Matchers.greaterThan(0L))
        body("updatedAt[0]", Matchers.greaterThan(0L))
    }
}
