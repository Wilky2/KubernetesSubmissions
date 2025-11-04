# Dummy Site

## [Dummy Site controller](./DummySiteController)

A Spring Boot application that creates all of the resources that are required for the functionality of a dummy site resource that takes a "website_url" as a parameter.

## [Dummy Site configuration](./config)

This directory contains all the configurations for the "dummy site".

## Deploy to kubernetes

```bash
kubectl apply -k config
```