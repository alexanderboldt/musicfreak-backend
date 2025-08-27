package com.alex.musicfreak.util

val Resource.Path.ALBUM_ID: String
    get() = "${Resource.Path.ALBUM}/${Resource.Path.ID}"

val Resource.Path.ARTIST_ID: String
    get() = "${Resource.Path.ARTIST}/${Resource.Path.ID}"