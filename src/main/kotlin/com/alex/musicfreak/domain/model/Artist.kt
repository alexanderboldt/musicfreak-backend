package com.alex.musicfreak.domain.model

import java.sql.Timestamp

data class Artist(
    val id: Long?,
    val name: String,
    val imagePath: String?,
    val createdAt: Timestamp?,
    val updatedAt: Timestamp?
)
