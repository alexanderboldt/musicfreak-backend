package com.alex.musicfreak.util

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.AlbumRequest
import com.alex.musicfreak.domain.AlbumResponse
import com.alex.musicfreak.domain.ArtistRequest
import com.alex.musicfreak.domain.ArtistResponse
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

// region artist

infix fun List<ArtistResponse>.shouldBeArtists(expected: List<ArtistRequest>) {
    zip(expected).forEach { (artistActual, artistExpected) ->
        artistActual shouldBeArtist artistExpected
    }
}

infix fun ArtistResponse.shouldBeArtist(expected: ArtistRequest) {
    id shouldBeGreaterThan 0
    userId shouldBe Fixtures.User.USER_ID
    name shouldBe expected.name
    filename.shouldBeNull()
    createdAt.shouldNotBeNull()
    updatedAt.shouldNotBeNull()
}

// endregion

// region album

infix fun List<AlbumResponse>.shouldBeAlbums(expected: List<AlbumRequest>) {
    zip(expected).forEach { (albumActual, albumExpected) ->
        albumActual shouldBeAlbum albumExpected
    }
}

infix fun AlbumResponse.shouldBeAlbum(expected: AlbumRequest) {
    id shouldBeGreaterThan 0
    userId shouldBe Fixtures.User.USER_ID
    artistId shouldBe expected.artistId
    name shouldBe expected.name
    year shouldBe expected.year
    tracks shouldBe expected.tracks
    filename.shouldBeNull()
    createdAt.shouldNotBeNull()
    updatedAt.shouldNotBeNull()
}

// endregion
