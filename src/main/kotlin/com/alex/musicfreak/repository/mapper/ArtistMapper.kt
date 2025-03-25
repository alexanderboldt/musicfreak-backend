package com.alex.musicfreak.repository.mapper

import com.alex.musicfreak.repository.api.ApiModelArtist
import com.alex.musicfreak.repository.database.DbModelArtist
import java.util.Date

// from api to database

fun ApiModelArtist.newDbModel() = DbModelArtist(0, name, Date().time, Date().time)

fun ApiModelArtist.mergeDbModel(existing: DbModelArtist) = DbModelArtist(existing.id, name, existing.createdAt, Date().time)

// from database to api

fun Iterable<DbModelArtist>.toApiModels() = map { it.toApiModel() }

fun DbModelArtist.toApiModel() = ApiModelArtist(id, name, createdAt, updatedAt)