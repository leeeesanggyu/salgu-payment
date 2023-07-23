./gradlew clean build
docker build -t salgu-payment:v1 .
docker rm -f salgu-payment