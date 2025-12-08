package org.musiccollection.mapper

import org.musiccollection.domain.AlbumResponse
import org.musiccollection.entity.AlbumEntity

// from entity to domain

fun AlbumEntity.toDomain() = AlbumResponse(
    id,
    userId,
    artistId,
    name,
    year,
    tracks,
    filename,
    createdAt,
    updatedAt
)
