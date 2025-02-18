FROM maven:3.9.9-eclipse-temurin-23-alpine AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package

FROM eclipse-temurin:21-jdk-alpine-3.21
WORKDIR /app
COPY --from=builder /app/target/*.jar api-gateway.jar
EXPOSE 8082
CMD ["java", "-jar", "api-gateway.jar"]
