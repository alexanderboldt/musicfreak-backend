package com.alex.musicfreak.controller

import io.restassured.RestAssured
import io.restassured.http.ContentType
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeEach

open class BaseControllerTest {

    @BeforeEach
    @Transactional
    fun beforeEach() {
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)
    }
}