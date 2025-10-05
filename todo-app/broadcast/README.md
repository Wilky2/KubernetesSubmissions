# Broadcast

An application that subscribes to NATS messages and sends the messages to Discord.

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
java -jar broadcast-0.0.1-SNAPSHOT.jar
```

---

## Run with Docker

1. **Build the Docker image:**

```bash
docker build -t broadcast .
```

2. **Run the container:**

```bash
docker run broadcast
```

---

## Deploy to kubernetes

```bash
kubectl apply -f manifests
```

## Environment variable

NATS_HOST: Adress to connect to nats server

TODO_CREATED_SUBJECT : NATS subject used to subscribe to messages when a new todo is created

TODO_COMPLETED_SUBJECT : NATS subject used to subscribe to messages when a todo is completed

DISCORD_WEBHOOK_URL : Discord webhook url uses by the app to send the message to discord
