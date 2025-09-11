package com.alex.musicfreak.resource

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.service.S3Service
import com.alex.musicfreak.repository.AlbumRepository
import com.alex.musicfreak.repository.ArtistRepository
import com.alex.musicfreak.service.UserService
import io.quarkus.test.InjectMock
import io.restassured.RestAssured
import io.restassured.http.ContentType
import jakarta.inject.Inject
import jakarta.transaction.Transactional
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.whenever

abstract class BaseResourceTest {

    @InjectMock
    lateinit var userService: UserService

    @Inject
    protected lateinit var s3Service: S3Service

    @Inject
    protected lateinit var artistRepository: ArtistRepository

    @Inject
    protected lateinit var albumRepository: AlbumRepository

    @BeforeEach
    @Transactional
    fun beforeEachBase() {
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)

        whenever(userService.userId).doReturn(Fixtures.User.USER_ID)
    }

    @AfterEach
    @Transactional
    fun afterEachBase() {
        artistRepository.deleteAll()
        albumRepository.deleteAll()
    }
}
