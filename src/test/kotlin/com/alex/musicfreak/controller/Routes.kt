package com.alex.musicfreak.controller

object Routes {
    object Artist {
        const val MAIN = "/api/v1/artists"
        const val DETAIL = "$MAIN/{id}"
    }

    object Album {
        const val MAIN = "/api/v1/albums"
        const val DETAIL = "$MAIN/{id}"
    }
}
