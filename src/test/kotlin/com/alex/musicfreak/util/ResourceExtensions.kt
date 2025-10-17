package com.alex.musicfreak.util

import com.alex.musicfreak.resource.Resource

val Resource.Path.ALBUM_ID: String
    get() = "$ALBUM/$ID"

val Resource.Path.ARTIST_ID: String
    get() = "$ARTIST/$ID"