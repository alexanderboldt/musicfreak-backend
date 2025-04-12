package com.alex.musicfreak.repository.api

data class ApiModelAlbum(
    val id: Long,
    val artistId: Long,
    val name: String,
    val year: Int,
    val tracks: Int
)
