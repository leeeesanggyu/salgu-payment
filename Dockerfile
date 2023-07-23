FROM openjdk:17-jdk-slim
RUN apt-get update && \
    apt-get install -y curl
ARG JAR_FILE=./build/libs/*-SNAPSHOT.jar
ENV PROFILE=dev
COPY ${JAR_FILE} app.jar
ENTRYPOINT java -Dspring.profiles.active=${PROFILE} -jar /app.jar