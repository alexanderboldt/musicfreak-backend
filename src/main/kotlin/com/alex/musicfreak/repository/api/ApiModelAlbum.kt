package com.alex.musicfreak.repository.api

import java.sql.Timestamp

data class ApiModelAlbum(
    val id: Long?,
    val artistId: Long?,
    val name: String?,
    val year: Int?,
    val tracks: Int?,
    val createdAt: Timestamp?,
    val updatedAt: Timestamp?
)
