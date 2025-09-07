# Todo Backend

Application that serves backend for the todo app.

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
java -jar todo-backend-0.0.1-SNAPSHOT.jar
```

---

## Run with Docker

1. **Build the Docker image:**

```bash
docker build -t todobackend .
```

2. **Run the container:**

```bash
docker run todobackend
```

---

## Deploy to kubernetes

```bash
kubectl apply -f manifests/deployment.yaml
```

## Environment variable
PORT: Server port

REDIRECT_URL: URL to redirect the user to after saving a todo.

