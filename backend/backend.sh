#!/bin/bash

# Navigate to the Spring Boot project root folder
cd "$(dirname "$0")"

# Clean and package the application
mvn clean package

# Run the Spring Boot service
mvn spring-boot:run

# Run the Spring Boot application
java -jar target/weather-backend.jar
