package com.alex.musicfreak.mapper

import com.alex.musicfreak.Fixtures
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class ArtistMapperTest : StringSpec({

    "should map domain to entity" {
        val domain = Fixtures.Artist.Domain.korn
        val entity = domain.toEntity()

        entity.id shouldBe 0
        entity.name shouldBe domain.name
        entity.createdAt.shouldNotBeNull()
        entity.updatedAt.shouldNotBeNull()
    }

    "should combine a new domain with an existing entity" {
        val domain = Fixtures.Artist.Domain.slipknot
        val entity = Fixtures.Artist.Entity.korn

        val combined = domain + entity

        combined.id shouldBe entity.id
        combined.name shouldBe domain.name
        combined.createdAt shouldBe entity.createdAt
        combined.updatedAt shouldNotBe entity.updatedAt
    }

    "should map entity to domain" {
        val entity = Fixtures.Artist.Entity.korn
        val domain = entity.toDomain()

        domain.id shouldBe entity.id
        domain.name shouldBe entity.name
        domain.createdAt shouldBe entity.createdAt
        domain.updatedAt shouldBe entity.updatedAt
    }
})
