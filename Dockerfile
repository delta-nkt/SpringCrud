# Use Java 21 JDK on Alpine Linux
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy your built JAR into the container
COPY target/DEMO-INTEGRATION-0.0.1-SNAPSHOT.jar app.jar

# Run the application when container starts
ENTRYPOINT ["java", "-jar", "app.jar"]