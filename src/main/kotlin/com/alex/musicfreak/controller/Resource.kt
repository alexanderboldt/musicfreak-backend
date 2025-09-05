package com.alex.musicfreak.controller

/**
 * Contains convenient wordings for paths and parameters.
 */
object Resource {
    object Path {
        const val ALBUM = "v1/albums"
        const val ALBUM_IMAGE = "$ALBUM/{id}/images"
        const val ARTIST = "v1/artists"
        const val ARTIST_ALBUM = "$ARTIST/{id}/albums"
        const val ARTIST_IMAGE = "$ARTIST/{id}/images"
        const val ID = "{id}"
    }

    object Param {
        const val ID = "id"
        const val SORT = "sort"
    }
}
