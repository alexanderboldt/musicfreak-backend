package com.alex.musicfreak.domain.model

import java.time.Instant

data class Artist(
    val id: Long?,
    val name: String,
    val filename: String?,
    val createdAt: Instant?,
    val updatedAt: Instant?
)
