package com.alex.musicfreak

import com.alex.musicfreak.domain.model.Artist
import com.alex.musicfreak.domain.model.Album
import com.alex.musicfreak.repository.album.AlbumEntity
import com.alex.musicfreak.repository.artist.ArtistEntity
import java.sql.Timestamp
import java.time.Instant

object Fixtures {
    object Artist {
        object Domain {
            val korn = Artist(0, "Korn", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()))
            val slipknot = Artist(0, "Slipknot", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()))
            val all = listOf(korn, slipknot)
        }
        object Entity {
            val korn = ArtistEntity(0, "Korn", Timestamp.from(Instant.now()), Timestamp.from(Instant.now()))
        }
    }

    object Album {
        object Domain {
            val issues = Album(null, 10, "Issues", 1999, 16, null, null)
            val untouchables = Album(null, 10, "Untouchables", 2002, 14, null, null)
        }
        object Entity {
            val issues = AlbumEntity(0, 10, "Issues", 1999, 16, Timestamp.from(Instant.now()), Timestamp.from(Instant.now()))
        }
    }
}
