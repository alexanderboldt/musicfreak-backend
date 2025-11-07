plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.quarkus)
}

kotlin {
    jvmToolchain(21)
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // development libraries
    implementation(enforcedPlatform(libs.quarkus.platform))
    implementation(libs.kotlin.stdlib)
    implementation(libs.jackson.kotlin)

    implementation(libs.quarkus.kotlin)
    implementation(libs.quarkus.rest)
    implementation(libs.quarkus.rest.jackson)
    implementation(libs.quarkus.smallrye)

    implementation(libs.quarkus.hibernate.orm.panache)
    implementation(libs.quarkus.jdbc.postgresql)
    implementation(libs.quarkus.flyway)

    implementation(libs.quarkus.oidc)
    implementation(libs.quarkus.security)

    implementation(libs.quarkus.messaging.kafka)

    implementation(libs.quarkus.arc)

    implementation(libs.quarkus.config.yaml)

    implementation(libs.aws.s3)

    // test libraries
    testImplementation(libs.quarkus.junit5)
    testImplementation(libs.quarkus.junit5.mockito)
    testImplementation(libs.quarkus.test.security)

    testImplementation(libs.mockito.kotlin)

    testImplementation(libs.kotest.runner.junit)
    testImplementation(libs.kotest.assertions.core)

    testImplementation(libs.rest.assured)

    testImplementation(libs.testcontainers.junit.jupiter)
    testImplementation(libs.testcontainers.postgresql)
    testImplementation(libs.testcontainers.minio)
}

group = "com.alex"

tasks.withType<Test> {
    systemProperty("java.util.logging.manager", "org.jboss.logmanager.LogManager")
}

allOpen {
    annotation("jakarta.ws.rs.Path")
    annotation("jakarta.enterprise.context.ApplicationScoped")
    annotation("jakarta.persistence.Entity")
    annotation("io.quarkus.test.junit.QuarkusTest")
}
