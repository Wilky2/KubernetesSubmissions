# Log Output App

Kubernetes Controller that create all of the resources that are required for the functionality of a dummy site resource that takes a  "website_url" as a parameter.

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
java -jar DummySiteController-0.0.1-SNAPSHOT.jar
```

---

## Run with Docker

1. **Build the Docker image:**

```bash
docker build -t dummysite .
```

2. **Run the container:**

```bash
docker run dummysite
```
