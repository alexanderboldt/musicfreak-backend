package com.alex.musicfreak.mapper

import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.repository.album.AlbumEntity
import java.sql.Timestamp
import java.time.Instant

// from domain to entity

fun Album.toEntity() = AlbumEntity(
    0,
    artistId!!,
    name!!,
    year!!,
    tracks!!,
    Timestamp.from(Instant.now()),
    Timestamp.from(Instant.now())
)

operator fun Album.plus(existing: AlbumEntity) = AlbumEntity(
    existing.id,
    artistId!!,
    name!!,
    year!!,
    tracks!!,
    existing.createdAt,
    Timestamp.from(Instant.now())
)

// from entity to domain

fun AlbumEntity.toDomain() = Album(
    id,
    artistId,
    name,
    year,
    tracks,
    createdAt,
    updatedAt
)
