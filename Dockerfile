FROM maven:3.8.5-jdk-8 AS builder

LABEL maintainer="andygulin@hotmail.com"
LABEL stage="builder"

WORKDIR /usr/src/app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
COPY car.json .

RUN mvn clean package -DskipTests

FROM openjdk:8-jdk-slim

LABEL maintainer="andygulin@hotmail.com"
LABEL version="1.0"
LABEL description="Car Image View Service"

WORKDIR /app

COPY --from=builder /usr/src/app/target/CarImageView-1.0.jar app.jar
COPY --from=builder /usr/src/app/car.json car.json

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+ExitOnOutOfMemoryError"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=docker"]