package com.alex.musicfreak.repository

import com.alex.musicfreak.entity.ArtistEntity
import jakarta.enterprise.context.ApplicationScoped

@ApplicationScoped
class ArtistRepository : BaseRepository<ArtistEntity>
