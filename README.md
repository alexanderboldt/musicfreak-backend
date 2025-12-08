# Music Collection

This is a backend application for managing the music-library like artists and albums.

## Tech-Stack

### Development
- Kotlin
- Quarkus
- Hibernate
- Flyway
- PostgreSQL
- Keycloak
- MinIO

### Test
- RestAssured
- Mockito
- Kotest
- Docker-Testcontainer

### Buildsystem
- Gradle

## Install
1. Build the quarkus docker image:
```bash
./gradlew clean quarkusBuild
```
2. Create and start the containers:
```bash
docker compose up -d
```
