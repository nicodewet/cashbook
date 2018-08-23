# Docker multistage build

FROM gradle:jdk10 as compile
COPY --chown=gradle:gradle . /home/source/java
WORKDIR /home/source/java
RUN gradle clean build

FROM openjdk:10-jre-slim
WORKDIR /home/application/java
COPY --from=compile "/home/source/java/build/libs/cashbook-0.0.1-SNAPSHOT.jar" .
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/home/application/java/cashbook-0.0.1-SNAPSHOT.jar"]