package org.musiccollection.repository

import org.musiccollection.entity.ArtistEntity
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ArtistRepository : BaseRepository<ArtistEntity>
