package com.alex.musicfreak

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import org.hamcrest.CoreMatchers
import org.junit.jupiter.api.Test

@QuarkusTest
class GreetingResourceTest {

    @Test
    fun testHelloEndpoint() {
        RestAssured.given()
          .`when`().get("/hello")
          .then()
             .statusCode(200)
             .body(CoreMatchers.`is`("Hello from Quarkus REST"))
    }
}