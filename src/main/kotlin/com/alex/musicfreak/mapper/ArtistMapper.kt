package com.alex.musicfreak.mapper

import com.alex.musicfreak.domain.ArtistResponse
import com.alex.musicfreak.entity.ArtistEntity

// from entity to domain

fun ArtistEntity.toDomain() = ArtistResponse(
    id,
    userId,
    name,
    filename,
    createdAt,
    updatedAt
)
