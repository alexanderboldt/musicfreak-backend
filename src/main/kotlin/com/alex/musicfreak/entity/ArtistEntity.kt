package com.alex.musicfreak.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.time.Instant

@Entity
data class ArtistEntity(

    @Id
    @GeneratedValue
    val id: Long,

    var name: String,

    var filename: String?,

    val createdAt: Instant,

    var updatedAt: Instant
)