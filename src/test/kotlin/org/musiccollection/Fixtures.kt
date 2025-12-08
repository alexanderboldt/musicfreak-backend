package org.musiccollection

import org.musiccollection.domain.AlbumRequest
import org.musiccollection.domain.ArtistRequest
import java.io.File

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
        val issues = AlbumRequest(
            0,
            "Issues",
            1999,
            16
        )

        val untouchables = AlbumRequest(
            0,
            "Untouchables",
            2002,
            14
        )
    }

    val image: File = File.createTempFile("image", ".jpg").apply {
        writeText("Image Content")
        deleteOnExit()
    }
}
