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

## Endpoint

/pingpong : Endpoint to request for the "pong 0" response

## Environment variable
FILEPATH :  Absolute path where files (including pingpong data) are stored.

PINGPONG_PORT: The port on which the application runs.

DATABASE_URL: Hostname or IP address of the PostgreSQL server.

DATABASE_PORT: Port of the PostgreSQL server.

DATABASE_NAME: Name of the PostgreSQL database to connect to.

DATABASE_USERNAME: Username for authenticating to the PostgreSQL database.

DATABASE_PASSWORD: Password for authenticating to the PostgreSQL database.

