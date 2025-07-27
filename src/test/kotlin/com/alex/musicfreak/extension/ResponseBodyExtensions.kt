package com.alex.musicfreak.extension

import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.domain.model.Artist
import io.restassured.common.mapper.TypeRef
import io.restassured.response.ResponseBodyExtractionOptions

fun ResponseBodyExtractionOptions.asArtists(): List<Artist> = `as`(object : TypeRef<List<Artist>>() {})
fun ResponseBodyExtractionOptions.asArtist(): Artist = `as`(object : TypeRef<Artist>() {})
fun ResponseBodyExtractionOptions.asAlbums(): List<Album> = `as`(object : TypeRef<List<Album>>() {})
fun ResponseBodyExtractionOptions.asAlbum(): Album = `as`(object : TypeRef<Album>() {})