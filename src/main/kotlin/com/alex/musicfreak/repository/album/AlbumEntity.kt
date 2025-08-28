package com.alex.musicfreak.repository.album

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.Instant

@Entity
data class AlbumEntity(

    @Id
    @GeneratedValue
    val id: Long,

    var artistId: Long,

    var name: String,

    var year: Int,

    var tracks: Int,

    var filename: String?,

    val createdAt: Instant,

    var updatedAt: Instant
)
