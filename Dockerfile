# First stage: Build
FROM maven:3-eclipse-temurin-17 AS build

# Copy all project files to the container
COPY . .

# Build the project and skip tests
RUN mvn clean package -DskipTests

# Second stage: Runtime
FROM eclipse-temurin:17-alpine

# Copy the built JAR file from the build stage
COPY --from=build /target/kam-lead-management-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java","-jar", "app.jar"]
