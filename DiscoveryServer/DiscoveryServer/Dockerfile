FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
EXPOSE 8010
ADD ./target/DiscoveryServer-0.0.1-SNAPSHOT.jar discovery-server.jar

ENTRYPOINT ["java", "-jar", "discovery-server.jar"]