FROM amazoncorretto:17-alpine-jdk AS builder
WORKDIR /app
COPY ./ .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests=true

FROM eclipse-temurin:17.0.5_8-jre-focal
WORKDIR /app
COPY --from=builder /app/target/log-output-file-0.0.1-SNAPSHOT.jar /app/

ENTRYPOINT ["java", "-jar", "log-output-file-0.0.1-SNAPSHOT.jar"]