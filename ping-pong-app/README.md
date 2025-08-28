# Ping Pong App

Application that simply responds with &quot;pong 0&quot; to a GET request

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
java -jar ping-pong-app-0.0.1-SNAPSHOT.jar
```

---

## Run with Docker

1. **Build the Docker image:**

```bash
docker build -t ping-pong-app .
```

2. **Run the container:**

```bash
docker run ping-pong-app
```

---

## Deploy to kubernetes

```bash
kubectl apply -f manifests
```

## Endpoint

/pingpong : Endpoint to request for the "pong 0" response

