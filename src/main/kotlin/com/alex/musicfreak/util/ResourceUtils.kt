package com.alex.musicfreak.util

object Resource {
    object Path {
        const val ALBUMS = "api/v1/albums"
        const val ARTISTS_ALBUMS = "api/v1/artists/{id}/albums"
        const val ARTISTS = "api/v1/artists"

        const val ID = "{id}"
    }

    object Param {
        const val ID = "id"
        const val SORT = "sort"
    }
}
