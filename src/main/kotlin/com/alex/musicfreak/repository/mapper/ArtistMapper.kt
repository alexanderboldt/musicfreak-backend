package com.alex.musicfreak.repository.mapper

import com.alex.musicfreak.repository.api.ApiModelArtist
import com.alex.musicfreak.repository.database.artist.DbModelArtist
import java.sql.Timestamp
import java.time.Instant

// from api to database

fun ApiModelArtist.newDbModel() = DbModelArtist(
    0,
    name!!,
    Timestamp.from(Instant.now()),
    Timestamp.from(Instant.now())
)

fun ApiModelArtist.mergeDbModel(existing: DbModelArtist) = DbModelArtist(
    existing.id,
    name!!,
    existing.createdAt,
    Timestamp.from(Instant.now())
)

// from database to api

fun Iterable<DbModelArtist>.toApiModels() = map { it.toApiModel() }

fun DbModelArtist.toApiModel() = ApiModelArtist(
    id,
    name,
    createdAt,
    updatedAt
)
