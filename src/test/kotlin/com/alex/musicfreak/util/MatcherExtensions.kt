package com.alex.musicfreak.util

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
    id.shouldNotBeNull()
    id shouldBeGreaterThan 0
    name.shouldNotBeNull()
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
    id.shouldNotBeNull()
    id shouldBeGreaterThan 0
    artistId.shouldNotBeNull()
    artistId shouldBe expected.artistId
    name.shouldNotBeNull()
    name shouldBe expected.name
    year.shouldNotBeNull()
    year shouldBe expected.year
    tracks.shouldNotBeNull()
    tracks shouldBe expected.tracks
    filename shouldBe expected.filename
    createdAt.shouldNotBeNull()
    updatedAt.shouldNotBeNull()
}

// endregion
