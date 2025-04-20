# Stage 1: Build
# Use Maven with Amazon Corretto JDK 17
FROM maven:3.9.9-amazoncorretto-17 AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build the project without tests
RUN mvn clean package -DskipTests

# Stage 2: Run
# Use Amazon Corretto JDK 17 runtime image
FROM amazoncorretto:17.0.15

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
