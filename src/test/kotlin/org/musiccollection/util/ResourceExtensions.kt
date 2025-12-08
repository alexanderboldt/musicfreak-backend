package org.musiccollection.util

import org.musiccollection.resource.Resource

val Resource.Path.ALBUM_ID: String
    get() = "$ALBUM/$ID"

val Resource.Path.ARTIST_ID: String
    get() = "$ARTIST/$ID"