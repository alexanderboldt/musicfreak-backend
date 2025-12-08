package org.musiccollection.util

import org.musiccollection.domain.AlbumResponse
import org.musiccollection.domain.ArtistResponse
import io.restassured.common.mapper.TypeRef
import io.restassured.response.ResponseBodyExtractionOptions

fun ResponseBodyExtractionOptions.asArtists(): List<ArtistResponse> = `as`(object : TypeRef<List<ArtistResponse>>() {})
fun ResponseBodyExtractionOptions.asArtist(): ArtistResponse = `as`(object : TypeRef<ArtistResponse>() {})

fun ResponseBodyExtractionOptions.asAlbums(): List<AlbumResponse> = `as`(object : TypeRef<List<AlbumResponse>>() {})
fun ResponseBodyExtractionOptions.asAlbum(): AlbumResponse = `as`(object : TypeRef<AlbumResponse>() {})