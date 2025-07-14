package com.alex.musicfreak

import com.alex.musicfreak.domain.Artist
import java.sql.Timestamp
import java.time.Instant

object Fixtures {
    object Artists {
        val korn = Artist(1, "Korn", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()))
        val slipknot = Artist(2, "Slipknot", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()))

        val all = listOf(korn, slipknot)
    }
}
