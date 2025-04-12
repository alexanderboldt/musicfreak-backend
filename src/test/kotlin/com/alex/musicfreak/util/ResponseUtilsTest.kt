package com.alex.musicfreak.util

import com.alex.musicfreak.repository.api.ApiModelArtist
import com.alex.musicfreak.repository.api.ApiModelError
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isNull
import java.time.Instant

class ResponseUtilsTest {

    private object Artists {
        val korn = ApiModelArtist(1, "Korn",)
        val slipknot = ApiModelArtist(2, "Slipknot")

        val all = listOf(korn, slipknot)
    }

    @Test
    fun  testOkWithOneArtist() {
        val answer = Answer.ok(Artists.korn)

        expectThat(answer.status).isEqualTo(HttpStatus.SC_OK)
        expectThat(answer.entity).isEqualTo(Artists.korn)
    }

    @Test
    fun  testOkWithMultipleArtists() {
        val answer = Answer.ok(Artists.all)

        expectThat(answer.status).isEqualTo(HttpStatus.SC_OK)
        expectThat((answer.entity as List<ApiModelArtist>).size).isEqualTo(Artists.all.size)
        expectThat(answer.entity).isEqualTo(Artists.all)
    }

    @Test
    fun  testCreated() {
        val answer = Answer.created(Artists.korn)

        expectThat(answer.status).isEqualTo(HttpStatus.SC_CREATED)
        expectThat(answer.entity).isEqualTo(Artists.korn)
    }

    @Test
    fun  testNoContent() {
        val answer = Answer.noContent()

        expectThat(answer.status).isEqualTo(HttpStatus.SC_NO_CONTENT)
        expectThat(answer.entity).isNull()
    }

    @Test
    fun  testBadRequest() {
        val message = "id not found"
        val error = ApiModelError(HttpStatus.SC_BAD_REQUEST, message)

        val answer = Answer.badRequest(message)

        expectThat(answer.status).isEqualTo(HttpStatus.SC_BAD_REQUEST)
        expectThat(answer.entity).isEqualTo(error)
    }
}