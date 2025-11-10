# Greeter App

A Spring Boot application that responds with a HTTP GET request with a greeting.

---

## Prerequisites

* **Java 17**
* **Apache Maven 3.9.9**

(Optional for Docker users: [Docker installed](https://docs.docker.com/get-docker/))

---

## Build the Application

Use Maven to build the JAR file:

```bash
mvn clean package
```

This will generate the JAR file in the `target/` directory.

---

## Run the Application Locally

Navigate to the `target/` folder and run:

```bash
java -jar greeter-app-0.0.1-SNAPSHOT.jar
```

---

## Run with Docker

1. **Build the Docker image:**

```bash
docker build -t greeter-app .
```

2. **Run the container:**

```bash
docker run greeter-app
```

---

## Endpoint

/greeter : Endpoint to request the greeting message.

## Environment variable
VERSION:  Version of the app

PORT: Server port
