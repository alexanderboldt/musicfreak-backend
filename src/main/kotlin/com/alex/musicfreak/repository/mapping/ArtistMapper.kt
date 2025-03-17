package com.alex.musicfreak.repository.mapping

import com.alex.musicfreak.repository.api.ApiModelArtistGet
import com.alex.musicfreak.repository.api.ApiModelArtistPost
import com.alex.musicfreak.repository.database.DbModelArtist
import java.util.Date

// from api to database

fun ApiModelArtistPost.toDbModel() = DbModelArtist(0, name, Date().time, Date().time)

fun ApiModelArtistPost.toDbModel(dbModelExisting: DbModelArtist) = DbModelArtist(dbModelExisting.id, name, dbModelExisting.createdAt, Date().time)

// from database to api

fun Iterable<DbModelArtist>.toApiModelGet() = map { it.toApiModelGet() }

fun DbModelArtist.toApiModelGet() = ApiModelArtistGet(id, name, createdAt, updatedAt)