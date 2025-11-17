# ---------- Stage 1: Build the JAR ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven build files
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application (skip tests to be faster)
RUN mvn -q clean package -DskipTests


# ---------- Stage 2: Run the app ----------
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
