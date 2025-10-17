package com.alex.musicfreak.util

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.AlbumRequest
import com.alex.musicfreak.domain.AlbumResponse
import com.alex.musicfreak.domain.ArtistRequest
import com.alex.musicfreak.domain.ArtistResponse
import com.alex.musicfreak.resource.Resource
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.apache.http.HttpStatus

fun createAlbum(album: AlbumRequest): AlbumResponse {
    return Given {
        body(album)
    } When {
        post(Resource.Path.ALBUM)
    } Then {
        statusCode(HttpStatus.SC_CREATED)
    } Extract {
        asAlbum()
    }
}

fun createArtist(artist: ArtistRequest): ArtistResponse {
    return Given {
        body(artist)
    } When {
        post(Resource.Path.ARTIST)
    } Then {
        statusCode(HttpStatus.SC_CREATED)
    } Extract {
        asArtist()
    }
}

fun uploadAlbumImage(albumId: Long): AlbumResponse {
    return Given {
        multiPart("image", Fixtures.image)
        contentType(ContentType.MULTIPART)
    } When {
        post(Resource.Path.ALBUM_IMAGE, albumId)
    } Then {
        statusCode(HttpStatus.SC_OK)
    } Extract {
        asAlbum()
    }
}

fun uploadArtistImage(artistId: Long): ArtistResponse {
    return Given {
        multiPart("image", Fixtures.image)
        contentType(ContentType.MULTIPART)
    } When {
        post(Resource.Path.ARTIST_IMAGE, artistId)
    } Then {
        statusCode(HttpStatus.SC_OK)
    } Extract {
        asArtist()
    }
}
