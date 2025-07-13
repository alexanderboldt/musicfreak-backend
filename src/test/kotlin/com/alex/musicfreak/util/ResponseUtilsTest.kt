package com.alex.musicfreak.util

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.repository.api.ApiModelArtist
import com.alex.musicfreak.repository.api.ApiModelError
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.apache.http.HttpStatus

class ResponseUtilsTest : StringSpec({

    "should set response to ok" {
        val answer = Answer.ok(Fixtures.Artists.korn)

        answer.status shouldBe HttpStatus.SC_OK
        answer.entity shouldBe Fixtures.Artists.korn
    }

    "should set response to ok with multiple artists" {
        val answer = Answer.ok(Fixtures.Artists.all)

        answer.status shouldBe HttpStatus.SC_OK
        answer.entity as List<ApiModelArtist> shouldHaveSize Fixtures.Artists.all.size
        answer.entity shouldBe Fixtures.Artists.all
    }

    "should set response to created" {
        val answer = Answer.created(Fixtures.Artists.korn)

        answer.status shouldBe HttpStatus.SC_CREATED
        answer.entity shouldBe Fixtures.Artists.korn
    }

    "should set response to no-content" {
        val answer = Answer.noContent()

        answer.status shouldBe HttpStatus.SC_NO_CONTENT
        answer.entity.shouldBeNull()
    }

    "should set response to bad-request" {
        val message = "id not found"
        val error = ApiModelError(HttpStatus.SC_BAD_REQUEST, message)

        val answer = Answer.badRequest(message)

        answer.status shouldBe HttpStatus.SC_BAD_REQUEST
        answer.entity shouldBe error
    }
})
