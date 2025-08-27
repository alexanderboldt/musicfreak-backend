package com.alex.musicfreak.mapper

import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.repository.artist.ArtistEntity

// from entity to domain

fun ArtistEntity.toDomain() = Artist(
    id,
    name,
    filename,
    createdAt,
    updatedAt
)
