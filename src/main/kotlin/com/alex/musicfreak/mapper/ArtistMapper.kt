package com.alex.musicfreak.mapper

import com.alex.musicfreak.domain.Artist
import com.alex.musicfreak.entity.ArtistEntity

// from entity to domain

fun ArtistEntity.toDomain() = Artist(
    id,
    userId,
    name,
    filename,
    createdAt,
    updatedAt
)
