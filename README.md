# messaging-service

SMS notifications and security PIN — microservice of the **Plazoleta de comidas** challenge (PRAGMA).

## User stories

- HU14 — Notify order ready (SMS + security PIN)

## Stack

Java 21 · Spring Boot 3.5.x · Gradle 8.10.x · PostgreSQL · MapStruct · Lombok · springdoc-openapi · JUnit 5 + Mockito.

## Architecture

Hexagonal (ports & adapters): `domain` / `application` / `infrastructure`. Full conventions in `CLAUDE.md`.

## Run

```bash
./gradlew clean build
./gradlew bootRun   # http://localhost:8083 - Swagger UI at /swagger-ui.html
```
