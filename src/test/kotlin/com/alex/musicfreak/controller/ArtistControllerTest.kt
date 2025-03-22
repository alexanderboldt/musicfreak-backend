package com.alex.musicfreak.controller

import com.alex.musicfreak.repository.api.ApiModelArtistPost
import com.alex.musicfreak.repository.database.ArtistRepository
import io.quarkus.test.junit.QuarkusTest
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import jakarta.ws.rs.core.MediaType
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

@QuarkusTest
class ArtistControllerTest {

    @Inject
    private lateinit var artistRepository: ArtistRepository

    @AfterEach
    @Transactional
    fun afterEach() {
        artistRepository.deleteAll()
    }

    @Test
    fun testPost() {
        Given {
            accept(MediaType.APPLICATION_JSON)
            contentType(MediaType.APPLICATION_JSON)
            body(ApiModelArtistPost("Korn"))
        } When {
            post("/api/v1/artists")
        } Then {
            statusCode(201)
        }
    }

    @Test
    fun testGetAllWithEmptyResults() {
        When {
            get("/api/v1/artists")
        } Then {
            statusCode(200)
            body(equalTo("[]"))
        }
    }

    @Test
    fun testGetAllWithOneResult() {
        Given {
            accept(MediaType.APPLICATION_JSON)
            contentType(MediaType.APPLICATION_JSON)
            body(ApiModelArtistPost("Korn"))
        } When {
            post("/api/v1/artists")
        } Then {
            statusCode(201)
        }

        When {
            get("/api/v1/artists")
        } Then {
            statusCode(200)
        } Extract {
            this.body()
        }
    }
}