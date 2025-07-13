package com.alex.musicfreak

import com.alex.musicfreak.repository.api.ApiModelArtist
import java.sql.Timestamp
import java.time.Instant

object Fixtures {
    object Artists {
        val korn = ApiModelArtist(1, "Korn", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()))
        val slipknot = ApiModelArtist(2, "Slipknot", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()))

        val all = listOf(korn, slipknot)
    }
}
