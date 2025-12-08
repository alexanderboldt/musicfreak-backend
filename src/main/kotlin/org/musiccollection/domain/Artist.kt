package org.musiccollection.domain

import java.time.Instant

data class ArtistRequest(
    val name: String
)

data class ArtistResponse(
    val id: Long,
    val userId: String,
    val name: String,
    val filename: String?,
    val createdAt: Instant,
    val updatedAt: Instant
)
