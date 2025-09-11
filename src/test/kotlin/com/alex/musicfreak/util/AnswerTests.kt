package com.alex.musicfreak.util

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.resource.Answer
import com.alex.musicfreak.domain.ArtistResponse
import com.alex.musicfreak.domain.Error
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.apache.http.HttpStatus
import java.io.InputStream

class AnswerTests : StringSpec({

    "should set response to ok" {
        val answer = Answer.ok(Fixtures.Artist.korn)

        answer.status shouldBe HttpStatus.SC_OK
        answer.entity shouldBe Fixtures.Artist.korn
    }

    "should set response to ok with multiple artists" {
        val answer = Answer.ok(Fixtures.Artist.all)

        answer.status shouldBe HttpStatus.SC_OK
        answer.entity as List<ArtistResponse> shouldHaveSize Fixtures.Artist.all.size
        answer.entity shouldBe Fixtures.Artist.all
    }

    "should set response to ok with a stream" {
        val stream = InputStream.nullInputStream()
        val answer = Answer.file(stream, "image.png")

        answer.status shouldBe HttpStatus.SC_OK
        answer.entity shouldBe stream
    }

    "should set response to created" {
        val answer = Answer.created(Fixtures.Artist.korn)

        answer.status shouldBe HttpStatus.SC_CREATED
        answer.entity shouldBe Fixtures.Artist.korn
    }

    "should set response to no-content" {
        val answer = Answer.noContent()

        answer.status shouldBe HttpStatus.SC_NO_CONTENT
        answer.entity.shouldBeNull()
    }

    "should set response to bad-request" {
        val message = "Invalid input"
        val error = Error(HttpStatus.SC_BAD_REQUEST, message)

        val answer = Answer.badRequest()

        answer.status shouldBe HttpStatus.SC_BAD_REQUEST
        answer.entity shouldBe error
    }
})
