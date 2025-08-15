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

    val name: String,

    val imagePath: String?,

    val createdAt: Timestamp,

    val updatedAt: Timestamp
)
