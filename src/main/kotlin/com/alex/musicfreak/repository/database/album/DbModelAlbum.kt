package com.alex.musicfreak.repository.database.album

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class DbModelAlbum(

    @Id
    @GeneratedValue
    val id: Long,

    val artistId: Long,

    val name: String,

    val year: Int,

    val tracks: Int,

    val createdAt: Long,

    val updatedAt: Long
)
