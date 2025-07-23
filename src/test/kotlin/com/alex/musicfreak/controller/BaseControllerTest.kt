package com.alex.musicfreak.controller

import io.restassured.RestAssured
import io.restassured.http.ContentType
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

open class BaseControllerTest {

    companion object {
        @Container
        private val postgres = PostgreSQLContainer<Nothing>("postgres:17.4").apply {
            withDatabaseName("testDb")
            withUsername("test")
            withPassword("test")
        }

        @JvmStatic
        @BeforeAll
        fun beforeAllBase() {
            postgres.start()

            System.setProperty("quarkus.datasource.jdbc.url", postgres.jdbcUrl)
            System.setProperty("quarkus.datasource.username", postgres.username)
            System.setProperty("quarkus.datasource.password", postgres.password)
        }
    }

    @BeforeEach
    @Transactional
    fun beforeEachBase() {
        RestAssured.requestSpecification = RestAssured.given().contentType(ContentType.JSON)
    }
}
