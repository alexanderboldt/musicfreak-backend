package com.alex.musicfreak.controller

import com.alex.musicfreak.repository.api.ApiModelArtistPost
import com.alex.musicfreak.repository.database.ArtistRepository
import io.quarkus.test.junit.QuarkusTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.ValidatableResponse
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.MediaType
import org.apache.http.HttpStatus
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

@QuarkusTest
class ArtistControllerTest {

    @Inject
    private lateinit var artistRepository: ArtistRepository

    private object Routes {
        val main = "/api/v1/artists"
        val detail = "$main/{id}"
    }

    private object Artists {
        val korn = ApiModelArtistPost("Korn")
        val slipknot = ApiModelArtistPost("Slipknot")
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
            accept(MediaType.APPLICATION_JSON)
            contentType(MediaType.APPLICATION_JSON)
            body(Artists.korn)
        } When {
            post(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
            assertArtist(Artists.korn)
        }
    }

    // endregion

    // region read all

    @Test
    fun testGetAllWithNoArtists() {
        // execute and verify
        When {
            get(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(0))
        }
    }

    @Test
    fun testGetAllWithOneArtist() {
        // precondition: post an artist
        postArtist(Artists.korn)

        // execute and verify
        When {
            get(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_OK)
            body("size()", equalTo(1))
            assertArtistInArray(Artists.korn)
        }
    }

    // endregion

    // region read one

    @Test
    fun testGetOneWithInvalidId() {
        // precondition: post an artist
        postArtist(Artists.korn)

        // execute and verify
        When {
            get(Routes.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testGetOneWithValidId() {
        // precondition: post an artist
        val id = postArtist(Artists.korn)

        // execute and verify
        When {
            get(Routes.detail, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertArtist(Artists.korn)
        }
    }

    // endregion

    // region update

    @Test
    fun testUpdateWithInvalidId() {
        // precondition: post an artist
        postArtist(Artists.korn)

        // execute the update and verify
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Artists.slipknot)
        } When {
            put(Routes.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testUpdateWithValidId() {
        // precondition: post an artist
        val id = postArtist(Artists.korn)

        // execute the update and verify
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
            body(Artists.slipknot)
        } When {
            put(Routes.detail, id)
        } Then {
            statusCode(HttpStatus.SC_OK)
            assertArtist(Artists.slipknot)
        }
    }

    // endregion

    // region delete

    @Test
    fun testDeleteWithInvalidId() {
        // precondition: post an artist
        postArtist(Artists.korn)

        // execute the delete and verify
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
        } When {
            delete(Routes.detail, 100)
        } Then {
            statusCode(HttpStatus.SC_BAD_REQUEST)
        }
    }

    @Test
    fun testDeleteWithValidRecipe() {
        // precondition: post an artist
        val id = postArtist(Artists.korn)

        // execute the delete and verify
        Given {
            accept(ContentType.JSON)
            contentType(ContentType.JSON)
        } When {
            delete(Routes.detail, id)
        } Then {
            statusCode(HttpStatus.SC_NO_CONTENT)
        }
    }

    // endregion

    private fun postArtist(artist: ApiModelArtistPost): Int {
        return Given {
            accept(MediaType.APPLICATION_JSON)
            contentType(MediaType.APPLICATION_JSON)
            body(artist)
        } When {
            post(Routes.main)
        } Then {
            statusCode(HttpStatus.SC_CREATED)
        } Extract {
            path("id")
        }
    }

    private fun ValidatableResponse.assertArtist(artist: ApiModelArtistPost) {
        body("id", Matchers.greaterThan(0))
        body("name", equalTo(artist.name))
        body("createdAt", Matchers.greaterThan(0L))
        body("updatedAt", Matchers.greaterThan(0L))
    }

    private fun ValidatableResponse.assertArtistInArray(artist: ApiModelArtistPost) {
        body("id[0]", Matchers.greaterThan(0))
        body("name[0]", equalTo(artist.name))
        body("createdAt[0]", Matchers.greaterThan(0L))
        body("updatedAt[0]", Matchers.greaterThan(0L))
    }
}