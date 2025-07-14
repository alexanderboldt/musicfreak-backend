package com.alex.musicfreak.repository.mapper

import com.alex.musicfreak.repository.api.ApiModelAlbum
import com.alex.musicfreak.repository.database.album.DbModelAlbum
import java.sql.Timestamp
import java.time.Instant

// from api to database

fun ApiModelAlbum.newDbModel() = DbModelAlbum(
    0,
    artistId!!,
    name!!,
    year!!,
    tracks!!,
    Timestamp.from(Instant.now()),
    Timestamp.from(Instant.now())
)

fun ApiModelAlbum.mergeDbModel(existing: DbModelAlbum) = DbModelAlbum(
    existing.id,
    artistId!!,
    name!!,
    year!!,
    tracks!!,
    existing.createdAt,
    Timestamp.from(Instant.now())
)

// from database to api

fun Iterable<DbModelAlbum>.toApiModels() = map { it.toApiModel() }

fun DbModelAlbum.toApiModel() = ApiModelAlbum(
    id,
    artistId,
    name,
    year,
    tracks,
    createdAt,
    updatedAt
)
