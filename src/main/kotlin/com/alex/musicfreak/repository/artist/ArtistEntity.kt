package com.alex.musicfreak.repository.artist

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.sql.Timestamp

@Entity
data class ArtistEntity(

    @Id
    @GeneratedValue
    val id: Long,

    var name: String,

    var filename: String?,

    val createdAt: Timestamp,

    var updatedAt: Timestamp
)
