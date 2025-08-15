package com.alex.musicfreak.mapper

import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.repository.artist.ArtistEntity
import java.sql.Timestamp
import java.time.Instant

// from domain to entity

fun Artist.toEntity() = ArtistEntity(
    0,
    name,
    null,
    Timestamp.from(Instant.now()),
    Timestamp.from(Instant.now())
)

operator fun Artist.plus(existing: ArtistEntity) = ArtistEntity(
    existing.id,
    name,
    existing.imagePath,
    existing.createdAt,
    Timestamp.from(Instant.now())
)

// from entity to domain

fun ArtistEntity.toDomain() = Artist(
    id,
    name,
    imagePath,
    createdAt,
    updatedAt
)
