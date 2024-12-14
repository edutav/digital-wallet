# Use an official Maven image to build the application
FROM maven:3.8.6-openjdk-18 AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Use a lightweight OpenJDK image to run the application
FROM eclipse-temurin:18-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Set environment variable for database URL
ENV DATABASE_URL=jdbc:postgresql://postgres:5432/digital_wallet_db

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]