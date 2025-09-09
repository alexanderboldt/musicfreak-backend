package com.alex.musicfreak.domain

import java.time.Instant

data class ArtistResponse(
    val id: Long,
    val userId: String,
    val name: String,
    val filename: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)
