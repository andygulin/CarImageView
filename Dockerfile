FROM maven:3.8.5-jdk-8 AS builder

LABEL maintainer andygulin@hotmail.com

WORKDIR /usr/src

COPY . .

RUN mvn clean package

FROM openjdk:8-jdk-slim

COPY --from=builder /usr/src/target/CarImageView-1.0.jar /app/app.jar

EXPOSE 8080

ENTRYPOINT java -jar /app/app.jar --spring.profiles.active=docker