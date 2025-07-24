package com.alex.musicfreak.mapper

import com.alex.musicfreak.Fixtures
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class AlbumMapperTest : StringSpec({

    "should map domain to entity" {
        val domain = Fixtures.Album.Domain.issues
        val entity = domain.toEntity()

        entity.id shouldBe 0
        entity.artistId shouldBe domain.artistId
        entity.name shouldBe domain.name
        entity.year shouldBe domain.year
        entity.tracks shouldBe domain.tracks
        entity.createdAt.shouldNotBeNull()
        entity.updatedAt.shouldNotBeNull()
    }

    "should combine a new domain with an existing entity" {
        val domain = Fixtures.Album.Domain.untouchables
        val entity = Fixtures.Album.Entity.issues

        val combined = domain + entity

        combined.id shouldBe entity.id
        combined.artistId shouldBe domain.artistId
        combined.name shouldBe domain.name
        combined.year shouldBe domain.year
        combined.tracks shouldBe domain.tracks
        combined.createdAt shouldBe entity.createdAt
        combined.updatedAt shouldNotBe entity.updatedAt
    }

    "should map entity to domain" {
        val entity = Fixtures.Album.Entity.issues
        val domain = entity.toDomain()

        domain.id shouldBe entity.id
        domain.artistId shouldBe domain.artistId
        domain.name shouldBe domain.name
        domain.year shouldBe domain.year
        domain.tracks shouldBe domain.tracks
        domain.createdAt shouldBe entity.createdAt
        domain.updatedAt shouldBe entity.updatedAt
    }
})
