package com.alex.musicfreak.util

import com.alex.musicfreak.domain.AlbumResponse
import com.alex.musicfreak.domain.ArtistResponse
import io.restassured.common.mapper.TypeRef
import io.restassured.response.ResponseBodyExtractionOptions

fun ResponseBodyExtractionOptions.asArtists(): List<ArtistResponse> = `as`(object : TypeRef<List<ArtistResponse>>() {})
fun ResponseBodyExtractionOptions.asArtist(): ArtistResponse = `as`(object : TypeRef<ArtistResponse>() {})

fun ResponseBodyExtractionOptions.asAlbums(): List<AlbumResponse> = `as`(object : TypeRef<List<AlbumResponse>>() {})
fun ResponseBodyExtractionOptions.asAlbum(): AlbumResponse = `as`(object : TypeRef<AlbumResponse>() {})