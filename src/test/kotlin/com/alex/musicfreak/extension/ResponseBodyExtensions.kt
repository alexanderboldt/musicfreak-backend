package com.alex.musicfreak.extension

import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.domain.Artist
import io.restassured.common.mapper.TypeRef
import io.restassured.response.ResponseBodyExtractionOptions

fun ResponseBodyExtractionOptions.asArtists() = `as`(object : TypeRef<List<Artist>>() {})
fun ResponseBodyExtractionOptions.asArtist() = `as`(object : TypeRef<Artist>() {})
fun ResponseBodyExtractionOptions.asAlbums() = `as`(object : TypeRef<List<Album>>() {})
fun ResponseBodyExtractionOptions.asAlbum() = `as`(object : TypeRef<Album>() {})