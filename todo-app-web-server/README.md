# Todo App Web Server

A web server for a todo app create with Spring Boot.

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
java -jar todo-app-web-server-0.0.1-SNAPSHOT.jar
```

---

## Run with Docker

1. **Build the Docker image:**

```bash
docker build -t todoappwebserver .
```

2. **Run the container:**

```bash
docker run todoappwebserver
```

---

## Deploy to kubernetes

```bash
kubectl apply -f manifests/deployment.yaml
```

## Environment variable
PORT: Server port

FILEPATH :  The file directory


