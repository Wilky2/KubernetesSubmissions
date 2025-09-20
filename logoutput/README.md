# Log Output

## [Log Output File](./log-output-file)

Application that generates a random string on startup and writes a line with the random string and timestamp every 5 seconds into a file.

## [Log Output App](./log-output-app)

A Spring Boot application that reads that file and provides the content in the HTTP GET endpoint for the user to see.

## [Ping Pong App](./ping-pong-app)

Application that simply responds with "pong 0" to a GET request

## [Postgres DB](./postgres-db)

This directory contains the Kubernetes YAML manifests for the PostgreSQL database.

## [Exercises namespace](./namespace)

This directory contains the Kubernetes YAML manifests for the namespace "exercises".