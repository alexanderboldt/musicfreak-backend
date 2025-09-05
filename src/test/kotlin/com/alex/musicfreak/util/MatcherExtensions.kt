package com.alex.musicfreak.util

import com.alex.musicfreak.Fixtures
import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.domain.Artist
import io.kotest.matchers.comparables.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

// region artist

infix fun List<Artist>.shouldBeArtists(expected: List<Artist>) {
    zip(expected).forEach { (artistActual, artistExpected) ->
        artistActual shouldBeArtist artistExpected
    }
}

infix fun Artist.shouldBeArtist(expected: Artist) {
    id shouldBeGreaterThan 0
    userId shouldBe Fixtures.User.USER_ID
    name shouldBe expected.name
    filename shouldBe expected.filename
    createdAt.shouldNotBeNull()
    updatedAt.shouldNotBeNull()
}

// endregion

// region album

infix fun List<Album>.shouldBeAlbums(expected: List<Album>) {
    zip(expected).forEach { (albumActual, albumExpected) ->
        albumActual shouldBeAlbum albumExpected
    }
}

infix fun Album.shouldBeAlbum(expected: Album) {
    id shouldBeGreaterThan 0
    userId shouldBe Fixtures.User.USER_ID
    artistId shouldBe expected.artistId
    name shouldBe expected.name
    year shouldBe expected.year
    tracks shouldBe expected.tracks
    filename shouldBe expected.filename
    createdAt.shouldNotBeNull()
    updatedAt.shouldNotBeNull()
}

// endregion
