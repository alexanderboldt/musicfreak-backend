FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY build/quarkus-app/ .
EXPOSE 4000
ENTRYPOINT ["java", "-Dquarkus.profile=stage", "-jar", "quarkus-run.jar"]