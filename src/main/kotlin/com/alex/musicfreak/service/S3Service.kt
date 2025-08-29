package com.alex.musicfreak.service

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

/**
 * Manages the connection to the S3-storage.
 *
 * @param endpoint the endpoint as a [String] will be automatically injected from the configuration.
 * @param region the region as a [String] will be automatically injected from the configuration.
 * @param accessKey the access-key as a [String] will be automatically injected from the configuration.
 * @param secretKey the secret-key as a [String] will be automatically injected from the configuration.
 */
@ApplicationScoped
class S3Service(
    @ConfigProperty(name = "quarkus.s3.endpoint-override") endpoint: String,
    @ConfigProperty(name = "quarkus.s3.aws.region") region: String,
    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.access-key-id") accessKey: String,
    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.secret-access-key") secretKey: String
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

    /**
     * Will be triggered after the creation of this class. All available buckets will be created in S3.
     */
    @PostConstruct
    fun init() {
        // try to create all buckets, if they don't exist
        S3Bucket.entries.forEach {
            createBucket(it)
        }
    }

    /**
     * Creates the assigned bucket, if it doesn't exist yet.
     *
     * @param bucket the bucket to create as a [S3Bucket].
     */
    fun createBucket(bucket: S3Bucket) {
        val bucketExists = s3Client
            .listBuckets { it.build() }
            .buckets()
            .any { it.name() == bucket.bucketName }

        if (!bucketExists) {
            s3Client.createBucket { it.bucket(bucket.bucketName).build() }
        }
    }

    /**
     * Uploads a file to S3.
     *
     * @param bucket the bucket where to store as a [S3Bucket].
     * @param path the path of the file as a [Path].
     * @param filename the filename as a [String].
     * @return returns
     */
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

    /**
     * Downloads a file.
     *
     * @param bucket the bucket to download from as a [S3Bucket].
     * @param filename the filename of the file as a [String].
     * @return the result as a [InputStream].
     */
    fun downloadFile(bucket: S3Bucket, filename: String): InputStream {
        return s3Client.getObject {
            it.bucket(bucket.bucketName).key(filename).build()
        }
    }

    /**
     * Deletes a file.
     *
     * @param bucket the bucket to delete from as a [S3Bucket].
     * @param filename the filename of the file as a [String].
     */
    fun deleteFile(bucket: S3Bucket, filename: String) {
        s3Client.deleteObject {
            it.bucket(bucket.bucketName).key(filename).build()
        }
    }
}
