package com.alex.musicfreak.repository.api

import java.sql.Timestamp

data class ApiModelArtist(
    val id: Long?,
    val name: String?,
    val createdAt: Timestamp?,
    val updatedAt: Timestamp?
)
