# Log Output App

A Spring Boot application that generates a random string on startup, stores this string into memory, and outputs it every 5 seconds with a timestamp.

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
java -jar log-output-app-0.0.1-SNAPSHOT.jar
```

---

## Run with Docker

1. **Build the Docker image:**

```bash
docker build -t log-output-app .
```

2. **Run the container:**

```bash
docker run log-output-app
```

---

## Deploy to kubernetes

```bash
kubectl apply -f manifests/deployment.yaml
```




