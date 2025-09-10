package com.alex.musicfreak

import com.alex.musicfreak.domain.Album
import com.alex.musicfreak.domain.ArtistRequest

object Fixtures {
    object User {
        const val USER_ID = "12345"
    }

    object Artist {
        val korn = ArtistRequest("Korn")
        val slipknot = ArtistRequest("Slipknot")

        val all = listOf(korn, slipknot)
    }

    object Album {
        object Domain {
            val issues = Album(
                0,
                "",
                10,
                "Issues",
                1999,
                16,
                null,
                null,
                null
            )

            val untouchables = Album(
                0,
                "",
                10,
                "Untouchables",
                2002,
                14,
                null,
                null,
                null
            )
        }
    }
}
