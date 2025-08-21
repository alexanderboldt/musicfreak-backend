package com.alex.musicfreak.domain.model

import java.sql.Timestamp

data class Album(
    val id: Long?,
    val artistId: Long,
    val name: String,
    val year: Int,
    val tracks: Int,
    val filename: String?,
    val createdAt: Timestamp?,
    val updatedAt: Timestamp?
)
