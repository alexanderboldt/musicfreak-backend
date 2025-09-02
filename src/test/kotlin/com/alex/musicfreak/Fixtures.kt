package com.alex.musicfreak

import com.alex.musicfreak.domain.Artist
import com.alex.musicfreak.domain.Album
import java.time.Instant

object Fixtures {
    object Artist {
        object Domain {
            val korn = Artist(
                0,
                "Korn",
                null,
                Instant.now(),
                Instant.now()
            )

            val slipknot = Artist(
                0,
                "Slipknot",
                null,
                Instant.now(),
                Instant.now()
            )

            val all = listOf(korn, slipknot)
        }
    }

    object Album {
        object Domain {
            val issues = Album(
                0,
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
