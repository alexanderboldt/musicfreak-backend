package com.alex.musicfreak.testresource

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager
import org.testcontainers.containers.MinIOContainer

class MinioTestResource : QuarkusTestResourceLifecycleManager {

    private val minio = MinIOContainer("minio/minio:latest")
        .withUserName("testtest")
        .withPassword("testtest")

    override fun start(): Map<String, String> {
        minio.start()

        return buildMap {
            put("quarkus.s3.endpoint-override", minio.s3URL)
            put("quarkus.s3.aws.credentials.static-provider.access-key-id", minio.userName)
            put("quarkus.s3.aws.credentials.static-provider.secret-access-key", minio.password)
        }
    }

    override fun stop() {
        minio.stop()
    }
}
