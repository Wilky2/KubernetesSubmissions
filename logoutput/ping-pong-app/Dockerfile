FROM amazoncorretto:17-alpine-jdk AS builder
WORKDIR /app
COPY ./ .
RUN ./mvnw clean package -DskipTests=true

FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR /app
COPY --from=builder /app/target/ping-pong-app-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "ping-pong-app-0.0.1-SNAPSHOT.jar"]