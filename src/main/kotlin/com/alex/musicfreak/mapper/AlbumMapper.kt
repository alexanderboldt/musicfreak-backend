package com.alex.musicfreak.mapper

import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.entity.AlbumEntity

// from entity to domain

fun AlbumEntity.toDomain() = Album(
    id,
    artistId,
    name,
    year,
    tracks,
    filename,
    createdAt,
    updatedAt
)
