package com.alex.musicfreak.domain.service

import io.minio.MinioClient
import io.minio.PutObjectArgs
import io.minio.RemoveObjectArgs
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.jboss.resteasy.reactive.multipart.FileUpload
import java.io.FileInputStream
import java.util.UUID

@ApplicationScoped
class MinioService(
    @param:ConfigProperty(name = "minio.endpoint") private val endpoint: String,
    @param:ConfigProperty(name = "minio.access-key") private val accessKey: String,
    @param:ConfigProperty(name = "minio.secret-key") private val secretKey: String,
    @param:ConfigProperty(name = "minio.bucket-name") private val bucket: String,
) {

    private val minioClient = MinioClient.builder()
        .endpoint(endpoint)
        .credentials(accessKey, secretKey)
        .build()

    fun uploadFile(file: FileUpload): String {
        val extension = file.fileName().substringAfter(".")
        val filename = "${UUID.randomUUID()}.$extension"

        val stream = FileInputStream(file.uploadedFile().toFile())
        val size = file.uploadedFile().toFile().length()

        minioClient.putObject(
            PutObjectArgs
                .builder()
                .bucket(bucket)
                .`object`(filename)
                .stream(stream, size, -1)
                .contentType(file.contentType())
                .build()
        )

        return filename
    }

    fun deleteFile(path: String) {
        minioClient.removeObject(
            RemoveObjectArgs
                .builder()
                .bucket(bucket)
                .`object`(path)
                .build()
        )
    }
}
