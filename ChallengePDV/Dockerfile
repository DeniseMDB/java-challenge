FROM amazoncorretto:21-alpine-jdk
WORKDIR /app
ADD ./target/ChallengePDV-0.0.1-SNAPSHOT.jar challenge-pdv.jar

ENTRYPOINT ["java", "-jar", "challenge-pdv.jar"]