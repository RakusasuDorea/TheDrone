# Drone Delivery System

A REST API for managing a drone fleet and medication deliveries, built with Spring Boot.

## Tech Stack

- **Java 17**
- **Spring Boot 3.3.2** — web, data JPA, validation
- **PostgreSQL** — primary database
- **Hibernate** — ORM with auto schema update
- **Lombok** — boilerplate reduction (`@Data`, `@Slf4j`, `@RequiredArgsConstructor`)
- **MapStruct** — compile-time DTO mapping
- **SpringDoc OpenAPI** — Swagger UI

## Architecture

Layered architecture with three tiers:

```
Controller → Service → Repository
```

- **Controller** — handles HTTP requests and responses
- **Service** — contains all business logic (battery checks, weight limits, state transitions)
- **Repository** — database interaction via Spring Data JPA
- **DTOs** — separate request/response models from internal entities
- **Mappers** — MapStruct interfaces convert between entities and DTOs

## Prerequisites

- Java 17+
- Maven
- PostgreSQL running on `localhost:5432`

## Setup

**1. Clone the repository**
```bash
git clone https://github.com/RakusasuDorea/TheDrone.git
cd TheDrone
```

**2. Create the database**

Using DBeaver, Postman, or any PostgreSQL client:
```sql
CREATE DATABASE drone_db;
```

**3. Configure credentials**

Update `src/main/resources/application.properties` if your PostgreSQL password differs:
```properties
spring.datasource.password=your_password
```

**4. Build the project**
```bash
mvn clean install -DskipTests
```

**5. Run the application**
```bash
mvn spring-boot:run
```

The application starts at `http://localhost:8080` and seeds the database with sample drones and medications on first run.

## API Documentation

Swagger UI is available at:
```
http://localhost:8080/swagger-ui.html
```

OpenAPI JSON spec:
```
http://localhost:8080/v3/api-docs
```

## API Endpoints

All endpoints are prefixed with `/api/v1/drones`.

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/register` | Register a new drone |
| `GET` | `/{serialNumber}` | Get drone info |
| `GET` | `/available` | List all available (IDLE) drones |
| `POST` | `/{serialNumber}/load` | Load a medication onto a drone |
| `GET` | `/{serialNumber}/medications` | Get all medications on a drone |
| `POST` | `/{serialNumber}/deliver` | Dispatch drone for delivery |
| `POST` | `/{serialNumber}/return` | Return drone to IDLE state |
| `GET` | `/{serialNumber}/battery` | Get battery level |

## Drone States

```
IDLE → LOADING → LOADED → DELIVERING → DELIVERED → RETURNING → IDLE
```

## Drone Models and Weight Limits

| Model | Weight Limit |
|-------|-------------|
| Lightweight | 250g |
| Middleweight | 500g |
| Cruiserweight | 750g |
| Heavyweight | 1000g |

## Business Rules

- Battery must be at or above **25%** to load medications
- Total medication weight cannot exceed the drone's weight limit
- A drone must have medications loaded before it can be dispatched
- A drone cannot return if it still has medications loaded
- Battery decreases by 10% on each delivery cycle

## Running Tests

```bash
mvn test
```

## License

This project is licensed under the MIT License — see the [LICENSE](LICENSE) file for details.
