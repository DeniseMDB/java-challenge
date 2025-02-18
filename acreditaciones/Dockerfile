FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
ADD ./target/acreditaciones-0.0.1-SNAPSHOT.jar acreditaciones.jar

ENTRYPOINT ["java", "-jar", "acreditaciones.jar"]