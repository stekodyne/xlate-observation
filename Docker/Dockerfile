# Use latest openjdk:8 image as the base
FROM openjdk:8-jdk-alpine

MAINTAINER Steffen Kory <stekodyne@gmail.com>
EXPOSE 8080
VOLUME /tmp
ADD target/xlate-observation-0.0.1-SNAPSHOT.jar app.jar
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]