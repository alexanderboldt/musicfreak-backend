package com.alex.musicfreak.repository.mapper

import com.alex.musicfreak.repository.api.ApiModelAlbum
import com.alex.musicfreak.repository.database.album.DbModelAlbum
import java.util.Date


// from api to database

fun ApiModelAlbum.newDbModel() = DbModelAlbum(0, artistId, name, year, tracks)

fun ApiModelAlbum.mergeDbModel(existing: DbModelAlbum) = DbModelAlbum(existing.id, artistId, name, year, tracks)

// from database to api

fun Iterable<DbModelAlbum>.toApiModels() = map { it.toApiModel() }

fun DbModelAlbum.toApiModel() = ApiModelAlbum(id, artistId, name, year, tracks)
