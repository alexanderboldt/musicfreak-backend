package com.alex.musicfreak.domain

import java.sql.Timestamp

data class Artist(
    val id: Long?,
    val name: String?,
    val createdAt: Timestamp?,
    val updatedAt: Timestamp?
)
