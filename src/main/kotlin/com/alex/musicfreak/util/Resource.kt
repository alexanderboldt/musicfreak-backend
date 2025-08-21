package com.alex.musicfreak.util

object Resource {
    object Path {
        const val ALBUM = "api/v1/albums"
        const val ARTIST_ALBUM = "api/v1/artists/{id}/albums"
        const val ARTIST = "api/v1/artists"

        const val ID = "{id}"
        const val ID_IMAGE = "{id}/image"

        const val ALBUM_ID = "$ALBUM/$ID"
        const val ARTIST_ID = "$ARTIST/$ID"
    }

    object Param {
        const val ID = "id"
        const val SORT = "sort"
    }
}
