package com.alex.musicfreak.repository.album

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.sql.Timestamp

@Entity
data class AlbumEntity(

    @Id
    @GeneratedValue
    val id: Long,

    var artistId: Long,

    var name: String,

    var year: Int,

    var tracks: Int,

    val createdAt: Timestamp,

    var updatedAt: Timestamp
)
