package com.alex.musicfreak.mapper

import com.alex.musicfreak.Fixtures
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class ArtistMapperTest : StringSpec({

    "should map domain to entity" {
        val domain = Fixtures.Artist.Domain.korn
        val entity = domain.toEntity()

        entity.id shouldBe 0
        entity.name shouldBe domain.name
        entity.filename.shouldBeNull()
        entity.createdAt.shouldNotBeNull()
        entity.updatedAt.shouldNotBeNull()
    }

    "should map entity to domain" {
        val entity = Fixtures.Artist.Entity.korn
        val domain = entity.toDomain()

        domain.id shouldBe entity.id
        domain.name shouldBe entity.name
        domain.filename shouldBe entity.filename
        domain.createdAt shouldBe entity.createdAt
        domain.updatedAt shouldBe entity.updatedAt
    }
})
