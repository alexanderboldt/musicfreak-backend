package org.musiccollection.resource

import org.musiccollection.Fixtures
import org.musiccollection.service.S3Service
import org.musiccollection.repository.AlbumRepository
import org.musiccollection.repository.ArtistRepository
import org.musiccollection.service.UserService
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
