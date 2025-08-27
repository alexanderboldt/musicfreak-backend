package com.alex.musicfreak.domain.service

import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import org.eclipse.microprofile.config.inject.ConfigProperty
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.io.InputStream
import java.net.URI
import java.nio.file.Path
import java.util.UUID

@ApplicationScoped
class S3Service(
    @param:ConfigProperty(name = "quarkus.s3.endpoint-override") private val endpoint: String,
    @param:ConfigProperty(name = "quarkus.s3.aws.region") private val region: String,
    @param:ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.access-key-id") private val accessKey: String,
    @param:ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.secret-access-key") private val secretKey: String
) {

    private val s3Client = S3Client.builder()
        .endpointOverride(URI.create(endpoint))
        .region(Region.of(region))
        .forcePathStyle(true)
        .credentialsProvider(
            StaticCredentialsProvider.create(
                AwsBasicCredentials.create(accessKey, secretKey)
            )
        ).build()

    @PostConstruct
    fun init() {
        // try to create all buckets, if they don't exist
        S3Bucket.entries.forEach {
            createBucket(it)
        }
    }

    fun createBucket(bucket: S3Bucket) {
        val bucketExists = s3Client
            .listBuckets { it.build() }
            .buckets()
            .any { it.name() == bucket.bucketName }

        if (!bucketExists) {
            s3Client.createBucket { it.bucket(bucket.bucketName).build() }
        }
    }

    fun uploadFile(bucket: S3Bucket, path: Path, filename: String): String {
        val extension = filename
            .substringAfterLast(".", "")
            .let { if(it.isNotBlank()) ".$it" else "" }

        val filename = "${UUID.randomUUID()}$extension"

        s3Client.putObject(
            { it.bucket(bucket.bucketName).key(filename).build() },
            RequestBody.fromFile(path)
        )

        return filename
    }

    fun downloadFile(bucket: S3Bucket, filename: String): InputStream {
        return s3Client.getObject {
            it.bucket(bucket.bucketName).key(filename).build()
        }
    }

    fun deleteFile(bucket: S3Bucket, filename: String) {
        s3Client.deleteObject {
            it.bucket(bucket.bucketName).key(filename).build()
        }
    }
}
