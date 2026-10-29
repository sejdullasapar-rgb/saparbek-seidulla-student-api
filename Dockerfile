# ---------- Stage 1: build the JAR with Gradle on a full JDK ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copy the Gradle wrapper and build scripts first so dependencies are cached
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

# Copy sources and build the executable Spring Boot JAR
COPY src ./src
RUN ./gradlew --no-daemon bootJar -x test

# ---------- Stage 2: small runtime image with JRE only ----------
FROM eclipse-temurin:21-jre
LABEL org.opencontainers.image.title="saparbek-seidulla-student-api" \
      org.opencontainers.image.authors="Saparbek Seidulla" \
      org.opencontainers.image.description="Spring Boot Student CRUD API by Saparbek Seidulla"

WORKDIR /app
RUN groupadd --system app && useradd --system --gid app --home /app app
COPY --from=build /workspace/build/libs/app.jar /app/app.jar
USER app

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
