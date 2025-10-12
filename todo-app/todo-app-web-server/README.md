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

## Environment variable
PORT: Server port

FILEPATH: The file directory

TODO_BACKEND_URL: URL to get the todo list from the backend

FORM_URL: URL the form submits the todo to.

IMG_NAME: Name of the download image in the file directory

IMG_URL: Url to download the image

IMG_SIZE_MIN: Minimum size of the image in pixels

IMG_SIZE_MAX: Maximum size of the image in pixels