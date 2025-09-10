package com.alex.musicfreak.mapper

import com.alex.musicfreak.domain.AlbumResponse
import com.alex.musicfreak.entity.AlbumEntity

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
