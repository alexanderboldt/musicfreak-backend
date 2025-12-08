package org.musiccollection.mapper

import org.musiccollection.domain.ArtistResponse
import org.musiccollection.entity.ArtistEntity

// from entity to domain

fun ArtistEntity.toDomain() = ArtistResponse(
    id,
    userId,
    name,
    filename,
    createdAt,
    updatedAt
)
