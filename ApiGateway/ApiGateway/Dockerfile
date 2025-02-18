FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
EXPOSE 8082
ADD ./target/ApiGateway-0.0.1-SNAPSHOT.jar api-gateway.jar

ENTRYPOINT [ "java", "-jar", "api-gateway.jar" ]