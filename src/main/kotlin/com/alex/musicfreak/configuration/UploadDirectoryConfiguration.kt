package com.alex.musicfreak.configuration

import io.quarkus.runtime.Startup
import jakarta.annotation.PostConstruct
import org.eclipse.microprofile.config.inject.ConfigProperty
import java.nio.file.Files
import kotlin.io.path.Path
import kotlin.io.path.notExists

@Startup
class UploadDirectoryConfiguration(@param:ConfigProperty(name = "values.path.albums") val pathAlbums: String) {

    @PostConstruct
    fun init() {
        val path = Path(pathAlbums)
        if (path.notExists()) Files.createDirectories(path)
    }
}
