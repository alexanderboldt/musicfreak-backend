package com.alex.musicfreak.domain

import java.time.Instant

data class AlbumResponse(
    val id: Long,
    val userId: String,
    val artistId: Long,
    val name: String,
    val year: Int,
    val tracks: Int,
    val filename: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)
