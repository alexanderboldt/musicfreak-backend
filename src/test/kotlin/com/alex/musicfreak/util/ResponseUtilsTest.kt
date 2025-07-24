package com.alex.musicfreak.util

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.Artist
import com.alex.musicfreak.domain.Error
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.apache.http.HttpStatus

class ResponseUtilsTest : StringSpec({

    "should set response to ok" {
        val answer = Answer.ok(Fixtures.Artist.Domain.korn)

        answer.status shouldBe HttpStatus.SC_OK
        answer.entity shouldBe Fixtures.Artist.Domain.korn
    }

    "should set response to ok with multiple artists" {
        val answer = Answer.ok(Fixtures.Artist.Domain.all)

        answer.status shouldBe HttpStatus.SC_OK
        answer.entity as List<Artist> shouldHaveSize Fixtures.Artist.Domain.all.size
        answer.entity shouldBe Fixtures.Artist.Domain.all
    }

    "should set response to created" {
        val answer = Answer.created(Fixtures.Artist.Domain.korn)

        answer.status shouldBe HttpStatus.SC_CREATED
        answer.entity shouldBe Fixtures.Artist.Domain.korn
    }

    "should set response to no-content" {
        val answer = Answer.noContent()

        answer.status shouldBe HttpStatus.SC_NO_CONTENT
        answer.entity.shouldBeNull()
    }

    "should set response to bad-request" {
        val message = "id not found"
        val error = Error(HttpStatus.SC_BAD_REQUEST, message)

        val answer = Answer.badRequest(message)

        answer.status shouldBe HttpStatus.SC_BAD_REQUEST
        answer.entity shouldBe error
    }
})
