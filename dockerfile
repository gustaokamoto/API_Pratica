# Build
FROM maven:3.1.2-openjdk-17 AS build

COPY . .
RUN mvn clean package -DskipTests

# Package
FROM openjdk:17-jdk-slim

COPY --from=build /target/API_Estoque-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]