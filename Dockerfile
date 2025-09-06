# stage 1: build
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew clean build -x test

# stage 2: run
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=builder /app/build/quarkus-app/ .
EXPOSE 4000
ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]
