package com.alex.musicfreak.repository.database.album

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.sql.Timestamp

@Entity
data class DbModelAlbum(

    @Id
    @GeneratedValue
    val id: Long,

    val artistId: Long,

    val name: String,

    val year: Int,

    val tracks: Int,

    val createdAt: Timestamp,

    val updatedAt: Timestamp
)
