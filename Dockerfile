# Use OpenJDK 17 Alpine for both building and running the application
FROM eclipse-temurin:17 as build

# Set the working directory in the Docker image
WORKDIR /app

# Copy the Gradle Wrapper scripts and the entire gradle folder
COPY ./initial/gradlew ./initial/gradlew.bat /app/
COPY ./initial/gradle /app/gradle
# Copy your build configuration files
COPY ./initial/build.gradle ./initial/settings.gradle /app/
# Copy your application source
COPY ./initial/src /app/src

# Give execution rights on the gradlew
RUN chmod +x /app/gradlew

# Build the application using the Gradle Wrapper
RUN /app/gradlew build

# Start a new stage for the final image
FROM eclipse-temurin:17 as final

# Copy the built JAR file from the build stage to the final image
COPY --from=build /app/build/libs/*.jar /app/spring-boot-0.0.1-SNAPSHOT-plain.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/spring-boot-0.0.1-SNAPSHOT-plain.jar"]
