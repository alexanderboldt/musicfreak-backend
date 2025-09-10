package com.alex.musicfreak.util

import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.domain.ArtistResponse
import io.restassured.common.mapper.TypeRef
import io.restassured.response.ResponseBodyExtractionOptions

fun ResponseBodyExtractionOptions.asArtists(): List<ArtistResponse> = `as`(object : TypeRef<List<ArtistResponse>>() {})
fun ResponseBodyExtractionOptions.asArtist(): ArtistResponse = `as`(object : TypeRef<ArtistResponse>() {})
fun ResponseBodyExtractionOptions.asAlbums(): List<Album> = `as`(object : TypeRef<List<Album>>() {})
fun ResponseBodyExtractionOptions.asAlbum(): Album = `as`(object : TypeRef<Album>() {})