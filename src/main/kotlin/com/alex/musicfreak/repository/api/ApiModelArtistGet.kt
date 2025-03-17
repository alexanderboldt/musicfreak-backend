package com.alex.musicfreak.repository.api

data class ApiModelArtistGet(
    val id: Long,
    val name: String,
    val createdAt: Long,
    val updatedAt: Long
)
