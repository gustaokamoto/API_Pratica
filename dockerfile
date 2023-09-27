# STAGE Build - servidor 1
FROM maven:3.8.3-openjdk-17 AS maquinabuild

COPY . .

RUN mvn clean package -DskipTests

# STAGE Packege / Deploy - servidor 2
FROM openjdk:17-jdk-slim

COPY --from=maquinabuild /target/API_Estoque-0.0.1-SNAPSHOT.jar gusta.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "gusta.jar"]