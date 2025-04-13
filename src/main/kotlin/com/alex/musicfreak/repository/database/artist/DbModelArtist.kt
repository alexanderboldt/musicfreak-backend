package com.alex.musicfreak.repository.database.artist

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import java.sql.Timestamp

@Entity
data class DbModelArtist(

    @Id
    @GeneratedValue
    val id: Long,

    val name: String,

    val createdAt: Timestamp,

    val updatedAt: Timestamp
)
