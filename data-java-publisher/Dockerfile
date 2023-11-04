# Use an official Maven image as the build environment
FROM maven:3.8.4-openjdk-8 AS builder
WORKDIR /app

# Copy the POM file and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests

# Create a new image for running the JAR
FROM openjdk:8-jre-alpine
WORKDIR /app

COPY --from=builder /app/target/data-java-publisher-1.0-SNAPSHOT-jar-with-dependencies.jar ./data-java-publisher.jar
COPY wait-for-mqtt.sh /app/wait-for-mqtt.sh
RUN chmod +x /app/wait-for-mqtt.sh

# Define the command to run your application, waiting for MQTT
CMD ["sh", "-c", "/app/wait-for-mqtt.sh && java -jar data-java-publisher.jar"]