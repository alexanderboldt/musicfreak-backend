package com.alex.musicfreak.domain

data class AlbumRequest(
    val artistId: Long,
    val name: String,
    val year: Int,
    val tracks: Int
)
