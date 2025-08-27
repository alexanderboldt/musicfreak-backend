package com.alex.musicfreak.mapper

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.repository.album.AlbumEntity

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
