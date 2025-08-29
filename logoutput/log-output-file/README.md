# Log Output file

Application that generates a random string on startup and writes a line with the random string and timestamp every 5 seconds into a file.

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
java -jar log-output-file-0.0.1-SNAPSHOT.jar
```

---

## Run with Docker

1. **Build the Docker image:**

```bash
docker build -t log-output-file .
```

2. **Run the container:**

```bash
docker run log-output-file
```

---

## Environment variable
FILEPATH :  The file directory
