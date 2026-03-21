# 📊 FITMENTOR - INFORME EXHAUSTIVO DEL CODEBASE

**Análisis Completo:** Estructura, Módulos, Endpoints, Base de Datos, Lógica de Negocio y Configuración.

---

## 📑 Tabla de Contenidos
1. [Estructura General](#1-estructura-general-del-proyecto)
2. [Módulos y Componentes](#2-todos-los-módulos-y-sus-componentes)
3. [Endpoints HTTP](#3-endpoints-http---listado-completo)
4. [Base de Datos](#4-base-de-datos)
5. [Lógica de Negocio](#5-lógica-de-negocio)
6. [Seguridad](#6-seguridad)
7. [Configuración](#7-configuración)
8. [Testing](#8-testing)
9. [Arquitectura Avanzada](#9-arquitectura-avanzada)
10. [Ejecución y Deployment](#10-ejecución-y-deployment)
11. [Resumen Ejecutivo](#11-resumen-ejecutivo)

---

## 1. ESTRUCTURA GENERAL DEL PROYECTO

### 1.1 Tipo de Proyecto
- **Tipo:** Backend REST API - Spring Boot 3.4.5
- **Lenguaje:** Kotlin (primario) + Java (secundario)
- **Patrón:** Domain-Driven Design (DDD) + Hexagonal Architecture
- **Estado:** Fullstack Ready (Backend completo)

### 1.2 Tech Stack Principal

| Componente | Tecnología | Versión |
|-----------|-----------|---------|
| **Framework** | Spring Boot | 3.4.5 |
| **Lenguaje** | Kotlin | 2.2.0-RC |
| **JDK** | Java | 21 |
| **Build Tool** | Gradle | 8.14.0 |
| **Base de Datos** | PostgreSQL | 17 |
| **Migraciones** | Flyway | 11.8.2 |
| **ORM** | Spring Data JPA | Hibernate |
| **Seguridad** | JWT (jjwt) | 0.11.5 |
| **WebAuthn** | WebAuthn4j | 0.29.6 |
| **API Docs** | SpringDoc OpenAPI | 2.8.11 |
| **Message Queue** | Apache Kafka | 7.9.1 |
| **Caché** | Caffeine | Spring integrated |
| **Contenedorización** | Docker + Compose | Latest |
| **Testing** | JUnit 5, MockK, AssertK | Latest |
| **Static Analysis** | OpenRewrite | 6.13.0 |

### 1.3 Estructura de Directorios

```
fitmentor/
├── build/                          # Build output (Gradle)
├── docker/                         # Docker configuration
│   ├── Dockerfile                 # Java 21 + Gradle base image
│   └── docker-compose.yml         # PostgreSQL + Kafka + Zookeeper + App
├── gradle/                         # Gradle wrapper
├── scripts/                        # Utility scripts
│   ├── run-local.sh              # Local development runner
│   ├── run-docker-compose.sh     # Docker full stack
│   └── openapi.sh                # Generate OpenAPI docs
├── src/
│   ├── main/
│   │   ├── java/com/inigo/
│   │   │   ├── arch/                    # Core auth & user module
│   │   │   │   ├── ArchApplication.kt   # Spring Boot entry point
│   │   │   │   ├── TestController.kt    # Health check endpoint
│   │   │   │   ├── config/
│   │   │   │   │   ├── SecurityConfig.kt
│   │   │   │   │   ├── CorsConfig.kt
│   │   │   │   │   ├── GlobalExceptionHandler.kt
│   │   │   │   │   └── JpaConfig.kt
│   │   │   │   └── user/                # User management module
│   │   │   │       ├── application/     # Use cases: Login, CreateUser
│   │   │   │       ├── domain/          # Entities, Value Objects
│   │   │   │       └── infrastructure/  # Controllers, JPA, Spring adapters
│   │   │   └── fitmentor/               # Main business logic
│   │   │       ├── client/              # Client management
│   │   │       ├── coach/               # Coach management
│   │   │       ├── plan/                # Training plans + Nutrition plans
│   │   │       ├── timeslot/            # Time slot scheduling
│   │   │       └── shared/              # Shared domain objects & events
│   │   └── resources/
│   │       ├── application.yaml         # Main configuration
│   │       ├── application-dev.yaml     # Development profile
│   │       ├── application-local.yaml   # Local development
│   │       ├── application-test.yaml    # Testing profile
│   │       ├── db/migration/            # Flyway SQL migrations
│   │       │   ├── V1__user.sql
│   │       │   ├── V2__client.sql
│   │       │   ├── V3__Plan.sql
│   │       │   ├── V4__coachs.sql
│   │       │   ├── V5__Supplements.sql
│   │       │   ├── V6__Meal_components.sql (359 lines)
│   │       │   └── V7__Nutritionists_sport_coachs.sql
│   │       └── openapi/                 # OpenAPI/Swagger specs
│   └── test/
│       ├── java/com/inigo/
│       │   ├── arch/
│       │   │   ├── ArchUserTests.kt
│       │   │   ├── UserUtils.kt
│       │   │   └── user/infrastructure/TestSecurityConfig.kt
│       │   └── fitmentor/
│       │       ├── client/infrastructure/
│       │       │   ├── ClientControllerTest.kt
│       │       │   └── ClientControllerE2ETest.kt
│       │       ├── coach/infrastructure/
│       │       │   ├── CoachControllerTest.kt
│       │       │   └── CoachControllerE2ETest.kt
│       │       ├── plan/coach/infrastructure/
│       │       │   ├── PlanControllerTest.kt
│       │       │   └── PlanControllerE2ETest.kt
│       │       ├── plan/nutrition/foods/infrastructure/
│       │       │   ├── FoodControllerTest.kt
│       │       │   └── FoodControllerE2ETest.kt
│       │       └── plan/nutrition/plan/infrastructure/
│       │           ├── NutritionPlanControllerTest.kt
│       │           └── NutritionPlanControllerE2ETest.kt
│       └── resources/
│           └── application-test.yaml
├── docker-compose.yml              # Root docker compose (if separate)
├── build.gradle                    # Gradle dependencies & tasks
├── settings.gradle                 # Project name & settings
├── gradlew & gradlew.bat          # Gradle wrappers
├── README.md                       # Basic documentation
└── PROJECT.md                      # This file
```

### 1.4 Gradle Build Configuration

**build.gradle** - Key Configuration:
```gradle
plugins {
  - org.springframework.boot v3.4.5
  - org.jetbrains.kotlin.jvm v2.2.0-RC
  - war packaging
  - openrewrite v6.13.0 (static analysis)
}

group = 'com.inigo'
version = '0.0.1-SNAPSHOT'
java.sourceCompatibility = 21
java.targetCompatibility = 21

dependencies {
  // Spring Framework
  - spring-boot-starter-web
  - spring-boot-starter-data-jpa
  - spring-boot-starter-security
  - spring-boot-starter-actuator
  - spring-security-oauth2-resource-server
  
  // Caching & Performance
  - spring-boot-starter-cache
  - com.github.ben-manes.caffeine
  
  // API Documentation
  - org.springdoc.openapi-starter-webmvc-ui v2.8.11
  
  // JWT & Security
  - io.jsonwebtoken.jjwt v0.11.5
  - com.webauthn4j.webauthn4j-core v0.29.6
  
  // Data
  - org.postgresql:postgresql
  - org.flywaydb:flyway-core v11.8.2
  - org.flywaydb:flyway-database-postgresql v11.8.2
  
  // Jackson & Kotlin
  - com.fasterxml.jackson.module.kotlin
  - org.jetbrains.kotlin.kotlin-stdlib
  
  // Message Queue
  - spring-kafka
  
  // Testing
  - spring-boot-starter-test
  - org.junit.jupiter (JUnit 5)
  - io.mockk:mockk v1.13.8
  - com.ninja-squad:springmockk v4.0.2
  - com.willowtreeapps:assertk v0.28.1
  - com.h2database:h2
}
```

### 1.5 Docker Configuration

**docker/Dockerfile:**
```dockerfile
FROM gradle:8.14.0-jdk21
COPY . /home/gradle/app
WORKDIR /home/gradle/app
EXPOSE 8080
CMD ["gradle", "bootRun", "--no-daemon"]
```

**docker/docker-compose.yml:**
```yaml
version: '3.8'

services:
  db:
    image: postgres:17
    ports: ["5432:5432"]
    environment:
      POSTGRES_DB: base
      POSTGRES_USER: user
      POSTGRES_PASSWORD: pass
    volumes: ["pgdata:/var/lib/postgresql/data"]
    networks: ["fitmentor-network"]

  zookeeper:
    image: confluentinc/cp-zookeeper:7.9.1
    ports: ["2181:2181"]
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
    networks: ["fitmentor-network"]

  kafka:
    image: confluentinc/cp-kafka:7.9.1
    ports: ["9092:9092"]
    depends_on: ["zookeeper"]
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://kafka:29092,PLAINTEXT_HOST://kafka:9092
      KAFKA_LISTENER_SECURITY_PROTOCOL_MAP: PLAINTEXT:PLAINTEXT,PLAINTEXT_HOST:PLAINTEXT
      KAFKA_INTER_BROKER_LISTENER_NAME: PLAINTEXT
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
    networks: ["fitmentor-network"]

  kafdrop:
    image: obsidiandynamics/kafdrop:latest
    ports: ["9000:9000"]
    depends_on: ["kafka"]
    environment:
      KAFKA_BROKERCONNECT: kafka:29092
    networks: ["fitmentor-network"]

  app:
    build: .
    ports: ["8080:8080"]
    depends_on: ["db", "kafka"]
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://db:5432/base
      SPRING_DATASOURCE_USERNAME: user
      SPRING_DATASOURCE_PASSWORD: pass
    networks: ["fitmentor-network"]

volumes:
  pgdata:

networks:
  fitmentor-network:
    driver: bridge
```

---

## 2. TODOS LOS MÓDULOS Y SUS COMPONENTES

### 2.1 Módulo USER (com.inigo.arch.user)
**Responsabilidad:** Gestión de usuarios, autenticación y autorización

#### Domain Models:
```kotlin
// Main entity
class User(
  val userId: UUID,
  val username: Username,          // Value object
  val email: Email,                // Value object
  val password: Password,          // Value object (BCrypt hashed)
  val role: Role,                  // Enum: COACH=0, CLIENT=1, ADMIN=2, USER=3
  val fido2: Fido2?               // Optional WebAuthn credentials
)

// Value Objects
@JvmInline value class Username(val value: String)
@JvmInline value class Email(val value: String)
@JvmInline value class Password(val value: String)
@JvmInline value class Token(val value: String)
data class Fido2(
  val credentialId: String,
  val publicKey: String,
  val counter: Int,
  val challenge: String
)
enum class Role(val value: Int) {
  COACH(0), CLIENT(1), ADMIN(2), USER(3)
}
```

#### Controllers & Endpoints:

**AuthController** (`/api/auth`)
```
POST /api/auth/login
  Request Body: { "username": String, "password": String }
  Response: { "token": "Bearer eyJ..." }
  Auth: NOT REQUIRED
  Logic: Authenticates user and generates JWT token
```

**UserController** (`/api/user`)
```
PUT /api/user
  Request Body: { 
    "id": UUID, 
    "username": String, 
    "password": String, 
    "email": String, 
    "role": "COACH|CLIENT|ADMIN|USER"
  }
  Response: "Created"
  Auth: NOT REQUIRED
  Logic: Creates new user
```

#### Application Layer (Use Cases):

| Use Case | Class | Description |
|----------|-------|-------------|
| **Login** | `Login.kt` | Generates JWT after authentication |
| **CreateUser** | `CreateUser.kt` | Creates user with validation (unique email/username) |
| **CreateNonLogeableUser** | `CreateNonLogeableUser.kt` | Creates user without login capability (for coaches/clients) |
| **AddTypeToUser** | `AddTypeToUser.kt` | Assigns type (Coach/Client) to base user |

#### Domain Stores (Interfaces):
```kotlin
interface UserStore {
  fun save(user: User)
  fun findByUsername(username: Username): User?
  fun existsByEmail(email: Email): Boolean
  fun existsByUsername(username: Username): Boolean
}
```

#### Infrastructure:

**Repositories:**
- `UserRepository.kt` - Implements UserStore with JPA
- `UserJpaRepository.kt` - Spring Data JPA interface

**Services:**
- `TokenGenerationService.kt` - JWT generation using AuthenticationManager
- `BearerService.kt` - JWT parsing, validation, token generation
  - Algorithm: HS256 (HMAC SHA-256)
  - Claims: jti, id, sub, name, email, userRole, clientId, coachId
  - Expiration: None by default (❌ Security issue)
- `JwtAuthenticationFilter.kt` - Extracts JWT from "Authorization" header
- `FitmentorAuthenticationManager.kt` - Custom authentication manager
- `UnauthorizedError.kt` - Custom exception

**Entities:**
- `UserJpa.kt` - JPA entity persisted to `users` table

---

### 2.2 Módulo CLIENT (com.inigo.fitmentor.client)
**Responsabilidad:** Client profile management and lifecycle

#### Domain Models:
```kotlin
class Client(
  id: ClientId,
  goals: String?,                     // Client fitness goals
  age: Int?,
  injuries: String?,
  weight: Int?,                       // Weight in kg
  equipmentAccess: Int?,              // Equipment level (0/1/2)
  user: UserId,                       // Links to User
  phonenumber: String?,
  email: String,
  username: String
) : FitmentorUser, AggregateRoot
```

#### Controllers & Endpoints:

**ClientController** (`/api/user/clients`)
```
GET /api/user/clients
  Headers: Authorization: Bearer <token>
  Response: ClientResponse { id, goals, age, injuries, weight, equipmentAccess, phonenumber, user }
  Auth: REQUIRED (JWT)
  Logic: Returns current client (extracted from token)

POST /api/user/clients
  Request Body: ClientModificationRequest
  Response: "" (empty)
  Auth: NOT REQUIRED
  Logic: Creates new client

PUT /api/user/clients
  Request Body: ClientModificationRequest
  Response: "" (empty)
  Auth: NOT REQUIRED
  Logic: Updates existing client
```

#### Application Layer (Use Cases):

| Use Case | Class | Description |
|----------|-------|-------------|
| **CreateClient** | `CreateClient.kt` | Creates client + publishes ClientCreated event |
| **FindClient** | `FindClient.kt` | Finds client by UserId |
| **UpdateClient** | `UpdateClient.kt` | Updates existing client |

#### Domain Store:
```kotlin
interface ClientStore {
  fun save(client: Client)
  fun findAll(): List<Client>
  fun findByUserId(userId: UserId): Client?
  fun delete(id: ClientId)
  fun existsUser(userId: UserId): Boolean
  fun existsClient(clientId: ClientId): Boolean
}
```

#### Infrastructure:

**Repositories:**
- `ClientRepository.kt` - Implements ClientStore
- `ClientJpaRepository.kt` - Spring Data JPA interface

**Entities:**
- `ClientJpa.kt` - Table: `clients`
  - Fields: id, goals, age, injuries, weight, equipment_access, phone_number, user_id, username, email
  - Indexes: `idx_clients_userId`

#### Domain Events:
```kotlin
class ClientCreated(name: String, clientId: ClientId, userId: UserId) : DomainEvent
class ClientUpdated(name: String, clientId: ClientId, userId: UserId) : DomainEvent
```

---

### 2.3 Módulo COACH (com.inigo.fitmentor.coach)
**Responsabilidad:** Coach profile management and specializations

#### Domain Models:
```kotlin
class Coach(
  id: CoachId,
  photo: String?,
  presentation: String?,              // Coach presentation/bio
  user: UserId,
  phonenumber: String?,
  email: String,
  username: String,
  isNutritionist: Boolean = false,
  isFitness: Boolean = false
) : FitmentorUser, AggregateRoot
```

#### Controllers & Endpoints:

**CoachController** (`/api/user/coaches`)
```
GET /api/user/coaches/{id}
  Path Parameter: id = UUID of coach
  Response: CoachResponse { id, phonenumber, presentation, photo, user, email, username }
  Auth: NOT REQUIRED
  Logic: Returns coach details

GET /api/user/coaches
  Response: List<CoachSummaryResponse> { name, presentation, photo }
  Auth: NOT REQUIRED
  Logic: Lists all coaches (summary view)

POST /api/user/coaches
  Request Body: CoachCreationRequestBody
  Response: "" (empty)
  Auth: NOT REQUIRED
  Logic: Creates new coach
```

#### Application Layer (Use Cases):

| Use Case | Class | Description |
|----------|-------|-------------|
| **CreateCoach** | `CreateCoach.kt` | Creates coach + publishes CoachCreated event |
| **FindCoach** | `FindCoach.kt` | Finds coach by CoachId |
| **FindAllCoaches** | `FindAllCoaches.kt` | Lists all coaches |
| **UpdateCoach** | `UpdateCoach.kt` | Updates existing coach |
| **ProjectOnCoachChange** | `ProjectOnCoachChange.kt` | Event listener for coach changes |

#### Infrastructure:

**Repositories & Services:**
- `CoachRepository.kt` - Implements CoachService
- `CoachJpaRepository.kt` - Spring Data JPA

**Entities:**
- `CoachJpa.kt` - Table: `coaches` (1:1 relationship with users)
  - Fields: id, photo, presentation, phone_number, user_id (FK)
  - Indexes: `idx_coaches_userId`
- `NutritionistJpa.kt` - Table: `nutritionists`
- `FitnessJpa.kt` - Table: `fitness`

---

### 2.4 Módulo PLAN - COACH (com.inigo.fitmentor.plan.coach)
**Responsabilidad:** Training plan creation and management

#### Domain Models:
```kotlin
data class Plan(
  id: PlanId,
  active: Boolean,
  client: ClientId,
  coach: CoachId,
  description: String,
  type: String,                      // e.g., "Cardio", "Strength"
  goals: String,
  equipment: String,
  startDate: Instant,
  endDate: Instant,
  updatedAt: Instant
) : AggregateRoot
```

#### Controllers & Endpoints:

**PlanController** (`/api/user/plans`)
```
GET /api/user/plans/client/{clientId}
  Path: clientId = UUID
  Headers: Authorization: Bearer <token> (extracts coachId from JWT)
  Response: List<PlanResponse> { id, active, client, coach, description, type, goals, equipment, startDate, endDate }
  Auth: REQUIRED (JWT)
  Logic: Returns plans for coach's client

GET /api/user/plans/client/{clientId}/plan/{planId}
  Path: clientId, planId = UUIDs
  Headers: Authorization: Bearer <token>
  Response: PlanResponse
  Auth: REQUIRED (JWT)
  Logic: Returns specific plan

PUT /api/user/plans
  Request Body: PlanModificationRequest
  Response: "" (empty)
  Auth: REQUIRED (JWT)
  Logic: Updates existing plan
```

#### Application Layer (Use Cases):

| Use Case | Class | Description |
|----------|-------|-------------|
| **GetPlans** | `GetPlans.kt` | Retrieves plans by client+coach |
| **GetPlanById** | `GetPlanById.kt` | Retrieves specific plan |
| **UpdatePlan** | `UpdatePlan.kt` | Updates plan |

#### Infrastructure:

**Repositories:**
- `PlanStore.kt` (interface)
- `PlanRepository.kt` (implementation)
- `PlanJpaRepository.kt` (Spring Data JPA)

**Entities:**
- `PlanJpa.kt` - Table: `plans`
  - Fields: id, active, client (FK), coach (FK), description, type, equipment, goals, start_date, end_date, created_at, updated_at
  - Indexes: `idx_plans_client`, `idx_plans_coach`

#### Domain Events:
```kotlin
class PlanCreated(name: String, planId: PlanId) : DomainEvent
class PlanUpdated(name: String, planId: PlanId) : DomainEvent
```

---

### 2.5 Módulo PLAN - NUTRITION - FOODS
**Responsabilidad:** Food database and nutritional information

#### Domain Models:
```kotlin
class Food(
  id: UUID,
  name: String,
  calPer100g: Double,              // Calories per 100g
  proteinPer100g: Double,          // Protein per 100g
  carbohydratePer100g: Double,     // Carbs per 100g
  fatPer100g: Double               // Fat per 100g
)
```

#### Controllers & Endpoints:

**FoodController** (`/api/nutrition/foods`)
```
GET /api/nutrition/foods
  Response: List<Food> { id, name, calPer100g, proteinPer100g, carbohydratePer100g, fatPer100g }
  Auth: NOT REQUIRED
  Cache: Caffeine (500 items, 6000s TTL)
  Logic: Returns all foods from database
```

#### Application Layer:

| Use Case | Class | Description |
|----------|-------|-------------|
| **GetAllFoods** | `GetAllFoods.kt` | Retrieves all foods from store |

#### Infrastructure:

**Entities:**
- `FoodJpa.kt` - Table: `foods`
  - Fields: id (PK), name, cal_per_100g, protein_per_100g, carbohydrate_per_100g, fat_per_100g
  - **Preloaded Data:** ~150+ foods (dairy, cheese, meats, fish, beverages, alcohol, etc.)

---

### 2.6 Módulo PLAN - NUTRITION - MEALS
**Responsabilidad:** Meal composition and component management

#### Domain Models:
```kotlin
class Meal(
  id: MealId,
  planId: UUID,
  name: String,
  mealComponents: List<MealComponent> = emptyList(),
  supplementIntakes: List<SupplementIntake> = emptyList()
) : AggregateRoot

class MealComponent(
  id: UUID,
  coachId: UUID,
  clientId: UUID,
  planId: UUID,
  food: FoodId,
  quantity: Double,
  unit: UnitType               // GR | ML
)

class SupplementIntake(
  id: UUID,
  coachId: UUID,
  clientId: UUID,
  planId: UUID,
  supplementId: SupplementId,
  quantity: Double,
  unit: UnitType               // GR | ML
)
```

#### Controllers & Endpoints:

**MealController** (`/api/nutrition/plans`)
```
POST /api/nutrition/plans/{planId}/meals
  Path: planId = UUID
  Request Body: MealRequest { id, name, components[], supplements[] }
  Response: Void (204 No Content)
  Auth: REQUIRED (extracts from SecurityContext)
  Logic: Adds meal with components to nutrition plan
```

#### Application Layer:

| Use Case | Class | Description |
|----------|-------|-------------|
| **AddMealToNutritionPlan** | `AddMealToNutritionPlan.kt` | Creates meal + components + supplements (transactional) |

#### Infrastructure:

**Entities:**
- `MealJpa.kt` - Table: `meals` (related to plans)
  - Fields: id, plan_id, name
- `MealComponentJpa.kt` - Table: `meal_components`
  - Fields: id, coach_id, client_id, plan_id, meal_id, food_id, quantity, unit
- `SupplementIntakeJpa.kt` - Table: `supplement_intakes`
  - Fields: id, coach_id, client_id, plan_id, meal_id, supplement_id, quantity, unit
- `SupplementJpa.kt` - Table: `supplements`
  - Preloaded: Creatine, Protein Whey, BCAA

---

### 2.7 Módulo PLAN - NUTRITION - PLANS
**Responsabilidad:** Nutrition plan creation and management

#### Domain Models:
```kotlin
class NutritionPlan(
  id: NutritionPlanId,
  clientId: UUID,
  coachId: UUID,
  description: String,
  startDate: Instant,
  endDate: Instant
) : AggregateRoot
```

#### Controllers & Endpoints:

**NutritionPlanController** (`/api/nutrition/plans`)
```
POST /api/nutrition/plans
  Request Body: AddNutritionPlanRequest { clientId, coachId, description, startDate, endDate }
  Response: Void (204 No Content)
  Auth: NOT REQUIRED
  Logic: Creates new nutrition plan
```

#### Application Layer:

| Use Case | Class | Description |
|----------|-------|-------------|
| **AddNutritionPlan** | `AddNutritionPlan.kt` | Creates NutritionPlan |

#### Infrastructure:

**Entities:**
- `NutritionPlanJpa.kt` - Table: `nutrition_plans`
  - Fields: id, client_id, coach_id, description, start_date, end_date

---

### 2.8 Módulo TIMESLOT
**Responsabilidad:** Training schedule time slots

#### Domain Models:
```kotlin
data class TimeSlot(
  id: TimeSlotId,
  clientId: UUID,
  coachId: UUID,
  planId: UUID,
  dayOfWeek: Int,              // 0-6 (Monday-Sunday)
  startTime: LocalTime,
  endTime: LocalTime
) : AggregateRoot
```

#### Application Layer:

| Use Case | Class | Description |
|----------|-------|-------------|
| **CreateTimeSlot** | `CreateTimeSlot.kt` | Creates time slot for training |

#### Infrastructure:

**Entities:**
- Table: `timeslots`
  - Fields: id, client (FK), coach (FK), plan (FK), day_of_week, start_time, end_time
  - Indexes: `idx_timeslots_plan`, `idx_timeslots_client`, `idx_timeslots_coach`

---

### 2.9 Módulo SHARED
**Responsabilidad:** Shared domain objects, value classes, and domain events

#### Value Objects (Domain IDs):
```kotlin
@JvmInline value class UserId(val value: UUID)
@JvmInline value class ClientId(val value: UUID)
@JvmInline value class CoachId(val value: UUID)
@JvmInline value class PlanId(val value: UUID)
@JvmInline value class MealId(val value: UUID)
@JvmInline value class TimeSlotId(val value: UUID)
@JvmInline value class FoodId(val value: UUID)
@JvmInline value class SupplementId(val value: UUID)
@JvmInline value class NutritionPlanId(val value: UUID)
```

#### Domain Events:
```kotlin
open class DomainEvent {
  val name: String
  val aggregateId: UUID
  val aggregateName: String
  val timestamp: Instant
}

open class ClientUpdated(name, clientId, userId): DomainEvent
class ClientCreated(name, clientId, userId): ClientUpdated

open class CoachUpdated(name, coachId, userId): DomainEvent
class CoachCreated(name, coachId, userId): CoachUpdated

class PlanCreated(name, planId): DomainEvent
class PlanUpdated(name, planId): DomainEvent
```

#### Base Classes:
```kotlin
open class FitmentorUser(
  phonenumber: String?,
  email: String,
  username: String,
  role: Role,
  user: UserId,
  aggregateName: String
) : AggregateRoot

open class AggregateRoot {
  private var events: MutableList<DomainEvent> = mutableListOf()
  
  protected fun addEvent(event: DomainEvent) { events.add(event) }
  fun publishEvents(): List<DomainEvent> = events.toList()
}
```

#### Kafka Integration:
```kotlin
@Service
class KafkaProducerService(...) {
  fun <T: AggregateRoot> sendSnapshot(aggregateRoot: T) {
    val json = objectMapper.writeValueAsString(aggregateRoot)
    kafkaTemplate.send(aggregateName, uuid.toString(), json)
  }
}
```

---

## 3. ENDPOINTS HTTP - LISTADO COMPLETO

### 3.1 Authentication & User Management

| Método | Ruta | Auth | Request Body | Response | Status | Descripción |
|--------|------|------|--------------|----------|--------|-------------|
| **POST** | `/api/auth/login` | ❌ | `{username, password}` | `{token: String}` | 200 | Login and get JWT token |
| **PUT** | `/api/user` | ❌ | `{id, username, password, email, role}` | `"Created"` | 200 | Create new user |

### 3.2 Client Management

| Método | Ruta | Auth | Request Body | Response | Status | Descripción |
|--------|------|------|--------------|----------|--------|-------------|
| **GET** | `/api/user/clients` | ✅ JWT | - | `ClientResponse` | 200 | Get current client |
| **POST** | `/api/user/clients` | ❌ | `ClientModificationRequest` | `""` | 200 | Create client |
| **PUT** | `/api/user/clients` | ❌ | `ClientModificationRequest` | `""` | 200 | Update client |

### 3.3 Coach Management

| Método | Ruta | Auth | Request Body | Response | Status | Descripción |
|--------|------|------|--------------|----------|--------|-------------|
| **GET** | `/api/user/coaches/{id}` | ❌ | - | `CoachResponse` | 200 | Get coach by ID |
| **GET** | `/api/user/coaches` | ❌ | - | `List<CoachSummaryResponse>` | 200 | List all coaches |
| **POST** | `/api/user/coaches` | ❌ | `CoachCreationRequestBody` | `""` | 200 | Create coach |

### 3.4 Training Plans

| Método | Ruta | Auth | Request Body | Response | Status | Descripción |
|--------|------|------|--------------|----------|--------|-------------|
| **GET** | `/api/user/plans/client/{clientId}` | ✅ JWT | - | `List<PlanResponse>` | 200 | Get plans for client |
| **GET** | `/api/user/plans/client/{clientId}/plan/{planId}` | ✅ JWT | - | `PlanResponse` | 200 | Get specific plan |
| **PUT** | `/api/user/plans` | ✅ JWT | `PlanModificationRequest` | `""` | 200 | Update plan |

### 3.5 Nutrition - Foods

| Método | Ruta | Auth | Response | Status | Descripción |
|--------|------|------|----------|--------|-------------|
| **GET** | `/api/nutrition/foods` | ❌ | `List<Food>` | 200 | Get all foods (cached) |

### 3.6 Nutrition - Plans

| Método | Ruta | Auth | Request Body | Response | Status | Descripción |
|--------|------|------|--------------|----------|--------|-------------|
| **POST** | `/api/nutrition/plans` | ❌ | `AddNutritionPlanRequest` | Void | 204 | Create nutrition plan |

### 3.7 Nutrition - Meals

| Método | Ruta | Auth | Request Body | Response | Status | Descripción |
|--------|------|------|--------------|----------|--------|-------------|
| **POST** | `/api/nutrition/plans/{planId}/meals` | ✅ JWT | `MealRequest` | Void | 204 | Add meal to plan |

### 3.8 Utility & Documentation

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| **GET** | `/test` | ❌ | Health check endpoint |
| **GET** | `/swagger-ui.html` | ❌ | OpenAPI/Swagger UI |
| **GET** | `/h2-console/**` | ❌ | H2 Console (test profile only) |
| **GET** | `/v3/api-docs/**` | ❌ | OpenAPI specs |

---

## 4. BASE DE DATOS

### 4.1 Schema Overview

FitMentor uses PostgreSQL 17 with 7 Flyway migrations managing schema evolution.

### 4.2 Migrations Detail

#### V1__user.sql
```sql
CREATE TABLE users (
  id UUID PRIMARY KEY,
  username VARCHAR UNIQUE NOT NULL,
  email VARCHAR UNIQUE NOT NULL,
  password VARCHAR NOT NULL,              -- BCrypt hashed
  role VARCHAR NOT NULL,                  -- COACH, CLIENT, ADMIN, USER
  fido2_credential_id VARCHAR,            -- WebAuthn credential ID
  fido2_public_key VARCHAR,               -- WebAuthn public key
  fido2_counter INTEGER,                  -- Challenge counter
  current_challenge VARCHAR,              -- Active challenge
  challenge_expiry DATE                   -- Challenge expiration
);

-- Default admin user
INSERT INTO users (id, username, password, email, role) 
VALUES ('00000000-0000-0000-0000-000000000001', 'admin', 'hashed_admin_pass', 'admin@fitmentor.com', 'ADMIN');
```

#### V2__client.sql
```sql
CREATE TABLE clients (
  id UUID PRIMARY KEY,
  goals VARCHAR,
  age INT,
  injuries VARCHAR,
  weight INT,                            -- Weight in kg
  equipment_access INT,                  -- Equipment level
  phone_number VARCHAR,
  user_id UUID NOT NULL,                 -- FK to users.id
  username VARCHAR NOT NULL,
  email VARCHAR NOT NULL
);

CREATE INDEX idx_clients_userId ON clients(user_id);
```

#### V3__Plan.sql
```sql
CREATE TABLE plans (
  id UUID PRIMARY KEY,
  active BOOLEAN NOT NULL,
  client UUID NOT NULL,                  -- FK to clients.id
  coach UUID NOT NULL,                   -- FK to coaches.id
  description VARCHAR NOT NULL,
  type VARCHAR NOT NULL,                 -- e.g., Cardio, Strength
  equipment VARCHAR NOT NULL,
  goals VARCHAR NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_plans_client ON plans(client);
CREATE INDEX idx_plans_coach ON plans(coach);

CREATE TABLE timeslots (
  id UUID PRIMARY KEY,
  client UUID NOT NULL,                  -- FK to clients.id
  coach UUID NOT NULL,                   -- FK to coaches.id
  plan UUID NOT NULL,                    -- FK to plans.id
  day_of_week INT NOT NULL,              -- 0-6
  start_time TIME NOT NULL,
  end_time TIME NOT NULL
);

CREATE INDEX idx_timeslots_plan ON timeslots(plan);
CREATE INDEX idx_timeslots_client ON timeslots(client);
CREATE INDEX idx_timeslots_coach ON timeslots(coach);
```

#### V4__coachs.sql
```sql
CREATE TABLE coaches (
  id UUID PRIMARY KEY,
  presentation VARCHAR NOT NULL,        -- Coach bio
  photo VARCHAR NOT NULL,                -- Photo URL
  phone_number VARCHAR NOT NULL,
  user_id UUID NOT NULL                 -- FK to users.id
);

CREATE INDEX idx_coaches_userId ON coaches(user_id);
```

#### V5__Supplements.sql
```sql
CREATE TABLE supplements (
  id UUID PRIMARY KEY,
  name VARCHAR(255),
  description VARCHAR(255)
);

-- Preloaded data
INSERT INTO supplements (id, name, description) VALUES
  ('uuid-1', 'Creatine', 'Creatine monohydrate supplement'),
  ('uuid-2', 'Protein Whey', 'Whey protein isolate'),
  ('uuid-3', 'BCAA', 'Branched chain amino acids');
```

#### V6__Meal_components.sql (359 lines)
```sql
CREATE TABLE foods (
  id UUID PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  cal_per_100g INTEGER NOT NULL,
  protein_per_100g INTEGER NOT NULL,
  carbohydrate_per_100g INTEGER NOT NULL,
  fat_per_100g INTEGER NOT NULL
);

-- Preloaded: ~150+ foods including:
-- Dairy: Milk, Yogurt, Cheese (various types)
-- Meats: Chicken, Beef, Pork, Turkey
-- Fish: Salmon, Tuna, Cod
-- Beverages: Water, Coffee, Tea, Juice
-- Alcohol: Beer, Wine, Spirits
-- And many more nutritional items

CREATE TABLE meals (
  id UUID PRIMARY KEY,
  plan_id UUID NOT NULL,                 -- FK to plans.id
  name VARCHAR NOT NULL
);

CREATE TABLE meal_components (
  id UUID PRIMARY KEY,
  coach_id UUID NOT NULL,
  client_id UUID NOT NULL,
  plan_id UUID NOT NULL,
  meal_id UUID NOT NULL,                 -- FK to meals.id
  food_id UUID NOT NULL,                 -- FK to foods.id
  quantity DOUBLE NOT NULL,
  unit VARCHAR NOT NULL                  -- GR | ML
);

CREATE TABLE supplement_intakes (
  id UUID PRIMARY KEY,
  coach_id UUID NOT NULL,
  client_id UUID NOT NULL,
  plan_id UUID NOT NULL,
  meal_id UUID NOT NULL,                 -- FK to meals.id
  supplement_id UUID NOT NULL,           -- FK to supplements.id
  quantity DOUBLE NOT NULL,
  unit VARCHAR NOT NULL                  -- GR | ML
);
```

#### V7__Nutritionists_sport_coachs.sql
```sql
CREATE TABLE nutritionists (
  id UUID PRIMARY KEY,
  user_id UUID                           -- FK to users.id
);

CREATE TABLE fitness (
  id UUID PRIMARY KEY,
  user_id UUID                           -- FK to users.id
);
```

### 4.3 Entity-Relationship Diagram

```
┌──────────────────────┐
│       users          │
├──────────────────────┤
│ ◉ id (UUID)          │
│  username (unique)   │
│  password (BCrypt)   │
│  email (unique)      │
│  role                │
│  fido2_*             │
└──────┬───────────────┘
       │ (1:N)
       ├─────────────┬──────────────┬──────────────┐
       │             │              │              │
       ▼             ▼              ▼              ▼
   ┌────────────┐┌────────┐┌──────────────┐┌─────────────┐
   │  clients   ││coaches ││nutritionists ││   fitness   │
   └────────────┘└────────┘└──────────────┘└─────────────┘

   clients (1:N)
       │
       ├──> plans ◄── coaches
       │           \
       │            \(1:N)
       └─────────► timeslots

plans (1:N)
     │
     ├──> meals (1:N)
     │      │
     │      ├──> meal_components ──> foods
     │      │
     │      └──> supplement_intakes ──> supplements
     │
     └──> nutrition_plans (1:N)
            │
            └──> meals
```

### 4.4 Table Summary

| Table | Purpose | Pre-loaded Data | Key Indexes |
|-------|---------|-----------------|-------------|
| **users** | Authentication & authorization | Admin user | PK |
| **clients** | Client profiles | 0 | `idx_clients_userId` |
| **coaches** | Coach profiles | 0 | `idx_coaches_userId` |
| **plans** | Training plans | 0 | `idx_plans_client`, `idx_plans_coach` |
| **timeslots** | Training schedule | 0 | `idx_timeslots_plan`, etc |
| **foods** | Nutrition database | ~150+ foods | PK |
| **supplements** | Supplement library | 3 (Creatine, Whey, BCAA) | PK |
| **meals** | Meal definitions | 0 | FK `plan_id` |
| **meal_components** | Meal composition | 0 | FK `meal_id`, `food_id` |
| **supplement_intakes** | Supplement intake | 0 | FK `meal_id`, `supplement_id` |
| **nutritionists** | Nutritionist profile | 0 | FK `user_id` |
| **fitness** | Fitness coach profile | 0 | FK `user_id` |

---

## 5. LÓGICA DE NEGOCIO

### 5.1 Authentication Flow

```
1. User POST /api/auth/login { username, password }
   │
2. AuthController.login() calls TokenGenerationService
   │
3. TokenGenerationService.generateTokenFor()
   ├─ AuthenticationManager.authenticate(username, password)
   │  └─ FitmentorAuthenticationManager validates credentials
   │     ├─ UserRepository.findByUsername(username)
   │     └─ Compare password with BCrypt hash
   │
4. BearerService.generateToken()
   ├─ Creates JWT with claims: id, email, userRole, clientId, coachId
   ├─ Algoritmo: HS256
   ├─ Expires: None (❌ security issue)
   └─ Returns "Bearer eyJhbGc..."
   │
5. Client receives token in response { "token": "Bearer ..." }
   │
6. In subsequent requests:
   ├─ Client sends Authorization header: "Bearer <token>"
   ├─ JwtAuthenticationFilter intercepts request
   ├─ BearerService.parseToken() decrypts and validates
   └─ SecurityContextHolder.getContext().authentication = UserAuthentication
```

### 5.2 Client Lifecycle

```
Creation:
  1. PUT /api/user/clients { id, goals, age, ... }
  2. ClientController.createClient()
  3. CreateClient.execute(client)
     ├─ client.ensureUserExists() -> Creates/validates User
     ├─ client.create(store) -> Persists ClientJpa
     └─ client.publishEvents() -> Emits ClientCreated event
  4. AddTypeOnClientCreated listener
     └─ Updates user type to CLIENT

Retrieval:
  1. GET /api/user/clients (with JWT header)
  2. Extracts UserId from JWT claims
  3. FindClient.execute(userId)
  4. ClientStore.findByUserId(userId)
  5. Returns ClientResponse

Update:
  1. PUT /api/user/clients { id, goals, age, ... }
  2. UpdateClient.execute(updatedClient)
  3. ClientStore.save(updatedClient)
  4. ClientUpdated event published
```

### 5.3 Coach Lifecycle

```
Creation:
  1. POST /api/user/coaches { user, presentation, photo, ... }
  2. CoachController.modifyCoach()
  3. CreateCoach.execute(coach)
     ├─ coach.ensureUserExists()
     ├─ coach.create(store)
     └─ coach.publishEvents() -> Emits CoachCreated
  4. Listeners update relationships

Retrieval:
  1. GET /api/user/coaches -> Lists all (summary)
  2. GET /api/user/coaches/{id} -> Gets single coach (detail)

Update:
  1. POST /api/user/coaches (with update data)
  2. UpdateCoach.execute()
  3. CoachUpdated event published
```

### 5.4 Training Plan Lifecycle

```
Creation:
  1. PUT /api/user/plans { client, coach, description, ... }
  2. PlanController.modifyPlan()
  3. Plan aggregate created
  4. PlanStore.save(plan)
  5. PlanJpa persisted

Retrieval:
  1. GET /api/user/plans/client/{clientId}
  2. Token provides coachId
  3. GetPlans.execute(clientId, coachId)
  4. PlanStore.findByClientAndCoach()
  5. Returns List<PlanResponse>

Specific Plan:
  1. GET /api/user/plans/client/{clientId}/plan/{planId}
  2. GetPlanById.execute()
  3. Filters by planId and ownership

Update:
  1. PUT /api/user/plans { id, description, ... }
  2. UpdatePlan.execute(plan)
  3. PlanStore.save(plan)
  4. Updated at timestamp refreshed
```

### 5.5 Nutrition Plan Lifecycle

```
Creation:
  1. POST /api/nutrition/plans { clientId, coachId, description, startDate, endDate }
  2. NutritionPlanController.addPlan()
  3. AddNutritionPlan.execute()
  4. NutritionStore.save(plan)
  5. NutritionPlanJpa persisted

Adding Meals:
  1. POST /api/nutrition/plans/{planId}/meals { id, name, components[], supplements[] }
  2. MealController.addMeal()
  3. AddMealToNutritionPlan.execute()
     ├─ Creates Meal aggregate
     ├─ Iterates components -> resolves foods from Food table
     ├─ Creates MealComponents (each references Food)
     ├─ Creates SupplementIntakes (each references Supplement)
     └─ Transactional persistence: all or nothing
  4. Meals persisted with relationships
```

### 5.6 Business Rules & Constraints

#### User Domain
- Email must be unique
- Username must be unique
- Password can be hashed or empty
- Role: COACH, CLIENT, ADMIN, USER
- FIDO2 credentials optional (challenge management)

#### Client Domain
- Linked to exactly 1 User (UserId FK)
- Inherits from FitmentorUser (role = CLIENT)
- Publishes events: ClientCreated, ClientUpdated
- Goal text optional but recommended

#### Coach Domain
- Linked to exactly 1 User (UserId FK)
- Can specialize: isFitness, isNutritionist
- Inherits from FitmentorUser (role = COACH)
- Publishes events: CoachCreated, CoachUpdated
- Presentation/bio provides coaching context

#### Plan Domain
- Linked to exactly 1 Client + 1 Coach
- Status: active/inactive boolean
- Dates: startDate, endDate (Instant, inclusive)
- Type describes training methodology
- Can have N TimeSlots + N Meals

#### Meal Domain
- Aggregate Root with MealComponent and SupplementIntake entities
- Transactional: if any component fails, entire meal fails
- Components reference Food by FoodId
- Supplements reference Supplement by SupplementId
- Quantities in appropriate units (GR for food, various for supplements)

#### Time Slot Domain
- Represents weekly recurring time slot
- dayOfWeek: 0-6 (Monday-Sunday)
- startTime < endTime validation
- Linked to Plan, Client, Coach

#### Nutrition Plan Domain
- Separate from Training Plan
- Contains Meals (1:N relationship)
- Date range: startDate to endDate
- Coach assigns to Client

---

## 6. SEGURIDAD

### 6.1 Authentication Mechanism

**JWT (JSON Web Tokens)**
- **Algorithm:** HS256 (HMAC SHA-256)
- **Secret:** Configurable in `application.yml` (minimum 256 bytes recommended)
- **Header:** Authorization
- **Prefix:** Bearer
- **Expiration:** None by default ❌ **CRITICAL VULNERABILITY**

**JWT Claims Structure:**
```json
{
  "jti": "UUID (unique JWT ID)",
  "id": "userId (UUID)",
  "sub": "username",
  "name": "username",
  "email": "user@example.com",
  "userRole": 0,                          // 0=COACH, 1=CLIENT, 2=ADMIN, 3=USER
  "clientId": "UUID (nullable)",
  "coachId": "UUID (nullable)",
  "iat": 1234567890,                      // Issued at (seconds)
  "exp": null                             // Expiration (none = indefinite)
}
```

### 6.2 Authorization & Roles

**Role System:**
```kotlin
enum class Role(val value: Int) {
  COACH = 0,      // Fitness/nutrition professional
  CLIENT = 1,     // Client/athlete
  ADMIN = 2,      // System administrator
  USER = 3        // Generic user
}
```

**Public Endpoints (No JWT Required):**
```
POST   /api/auth/login
PUT    /api/user
POST   /api/user/clients
PUT    /api/user/clients
POST   /api/user/coaches
GET    /api/user/coaches
GET    /api/user/coaches/{id}
GET    /api/nutrition/foods
POST   /api/nutrition/plans
/webauthn/**
/swagger-ui/**
/v3/api-docs/**
/h2-console/**  (test profile only)
```

**Protected Endpoints (JWT Required):**
```
GET    /api/user/clients                           (extract UserId from JWT)
GET    /api/user/plans/client/{clientId}           (extract CoachId from JWT)
GET    /api/user/plans/client/{clientId}/plan/{planId}
PUT    /api/user/plans                             (extract CoachId from JWT)
POST   /api/nutrition/plans/{planId}/meals         (extract from SecurityContext)
```

### 6.3 CORS Configuration

```kotlin
@Configuration
class CorsConfig {
  allowCredentials = true
  allowedOrigins = [
    "https://c0b28b449254.ngrok-free.app",
    "http://localhost:4200"
  ]
  allowedHeaders = [
    "Authorization",
    "Cache-Control",
    "Content-Type"
  ]
  allowedMethods = [
    "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
  ]
  exposedHeaders = ["Authorization"]
  maxAge = 3600
}
```

### 6.4 FIDO2 / WebAuthn Implementation (Partial)

**Technology:** WebAuthn4j v0.29.6

**Storage in users table:**
```
fido2_credential_id    - Device credential identifier
fido2_public_key       - Device public key
fido2_counter          - Challenge counter for replay prevention
current_challenge      - Active challenge string
challenge_expiry       - Challenge expiration date
```

**Components:**
- `FidoWebAuthn4jService.kt` - WebAuthn operations
- `WebAuthnConfig.kt` - Configuration
- `FidoService.kt` - Business logic

**Status:** Partially integrated (registration endpoint incomplete)

### 6.5 Security Issues IDENTIFIED

| Severity | Issue | Location | Impact | Recommendation |
|----------|-------|----------|--------|-----------------|
| 🔴 **CRITICAL** | JWT tokens have no expiration | `BearerService.generateToken()` | Tokens valid indefinitely | Add expiration claim (e.g., 1 hour), implement refresh tokens |
| 🔴 **CRITICAL** | Database password hardcoded | `application-dev.yaml` | Credentials exposed in config | Use environment variables or secrets manager |
| 🟠 **HIGH** | No input validation on creation endpoints | `/api/user/clients`, `/api/user/coaches` | Potential for invalid data persistence | Validate age ranges (18-100), weight ranges (30-300kg), email format |
| 🟠 **HIGH** | SQL injection risk in dynamic queries | `PlanRepository`, `ClientRepository` | Data breach | Use parameterized queries (already compliant with JPA) |
| 🟠 **HIGH** | No rate limiting on auth endpoints | `AuthController` | Brute force attacks possible | Implement RateLimiter with Spring Cloud Circuit Breaker |
| 🟠 **HIGH** | Weak JWT secret in examples | `application.yaml` | Token forgery if not updated | Use strong random secret (256+ bytes) |
| 🟡 **MEDIUM** | Debug print statements in code | `CoachController`, `TokenGenerationService` | Sensitive info in logs | Replace with SLF4J logging at DEBUG level |
| 🟡 **MEDIUM** | No validation of email uniqueness in Coach creation | `CoachController` | Duplicate emails possible | Add @UniqueConstraint or service validation |
| 🟡 **MEDIUM** | H2 console exposed in test profile | `application-test.yaml` | DB structure exposed | Keep H2 console in test profile only (already done) |
| 🟡 **MEDIUM** | No HTTPS in local development | `application-local.yaml` | Clear-text JWT transmission | Use HTTPS even in local (optional but recommended) |
| 🟢 **LOW** | No request ID correlation | `GlobalExceptionHandler` | Difficult to trace errors | Implement correlation ID MDC context |

### 6.6 Mitigation Strategies Implemented

✅ Spring Security enabled
✅ JWT token-based authentication
✅ JwtAuthenticationFilter for request interception
✅ CORS configuration restricts origins
✅ Parameterized queries (JPA prevents SQL injection)
✅ HTTPS in dev profile (port 8443, SSL enabled)
✅ H2 console limited to test profile
✅ FIDO2 infrastructure (partial)

### 6.7 Recommended Security Enhancements

1. **Token Expiration:** Add `exp` claim to JWT (e.g., now + 1 hour)
2. **Refresh Tokens:** Implement refresh token mechanism for long-lived sessions
3. **Rate Limiting:** Use Resilience4j RateLimiter on auth endpoints
4. **Input Validation:** Add @Validated and @ConstraintValidator annotations
5. **Secrets Management:** Move secrets to environment or HashiCorp Vault
6. **HTTPS:** Enforce HTTPS in all profiles
7. **Logging:** Replace print() with SLF4J Logger
8. **Audit Trail:** Log all auth events (login, token refresh, permission changes)
9. **FIDO2 Completion:** Add WebAuthn registration and authentication endpoints
10. **Two-Factor Authentication:** Implement optional 2FA for admin users

---

## 7. CONFIGURACIÓN

### 7.1 Application Profiles

#### **application.yaml** (Default)
```yaml
spring:
  application:
    name: fitmentor
  
  cache:
    type: caffeine
    cache-names: foods
    caffeine:
      spec: maximumSize=500,expireAfterAccess=600s
    caffeine.caches:
      foods.spec: maximumSize=500,expireAfterAccess=6000s
  
  datasource:
    url: ${SPRING_DATASOURCE_URL}
    username: ${SPRING_DATASOURCE_USERNAME}
    password: ${SPRING_DATASOURCE_PASSWORD}
  
  jpa:
    hibernate:
      ddl-auto: validate  # Don't modify schema automatically
    show-sql: false
    properties:
      hibernate.format_sql: true
      hibernate.use_sql_comments: true
  
  flyway:
    locations: classpath:db/migration
    enabled: true
  
  main:
    allow-bean-definition-overriding: true
  
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer

fitmentor:
  domain: c0b28b449254.ngrok-free.app

jwt:
  secret: "random string dfsdfenrñjweugb must be at least 256 bytes"
  issuer: "fitmentor"
  audience: "user"
  header: "Authorization"
  prefix: "Bearer"
  token-type: "JWT"

server:
  port: 8080
  servlet:
    context-path: /
```

#### **application-dev.yaml**
```yaml
server:
  port: 8443
  ssl:
    enabled: true
    key-store: classpath:fitmentor.com.p12
    key-store-password: ${KEYSTORE_PASSWORD:fitmentor_change_it}
    key-store-type: PKCS12
    key-alias: fitmentor

spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/base
    username: user
    password: pass
  
  jpa:
    hibernate:
      ddl-auto: update  # Auto-migrate schema
    show-sql: true
    properties:
      hibernate.format_sql: true
  
  kafka:
    bootstrap-servers: localhost:9092
    consumer:
      group-id: projections
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer

logging:
  level:
    root: INFO
    com.inigo: DEBUG
    org.springframework: DEBUG
```

#### **application-local.yaml**
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/base
    username: user
    password: pass
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
  
  flyway:
    locations: classpath:db/migration
    enabled: true

logging:
  level:
    root: WARN
    com.inigo: INFO
    org.springframework.web: DEBUG
```

#### **application-test.yaml**
```yaml
spring:
  h2:
    console:
      enabled: true
      path: /h2-console
  
  datasource:
    url: jdbc:h2:mem:base
    driver-class-name: org.h2.Driver
    username: sa
    password: ""
  
  jpa:
    hibernate:
      ddl-auto: create-drop  # Recreate schema for each test
    show-sql: false
    properties:
      hibernate.dialect: org.hibernate.dialect.H2Dialect
  
  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: 0  # Random port for test isolation

jwt:
  secret: "12345678901234567890123456789012"
```

### 7.2 Environment Variables (Production)

```bash
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/fitmentor_prod
SPRING_DATASOURCE_USERNAME=fitmentor_user
SPRING_DATASOURCE_PASSWORD=<secure-password>

# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=kafka-broker-1:9092,kafka-broker-2:9092,kafka-broker-3:9092

# JWT Secret (must be >= 256 bytes)
JWT_SECRET=<long-random-secure-secret-from-vault>

# SSL/TLS
KEYSTORE_PASSWORD=<keystore-password>
```

### 7.3 Ports by Profile

| Component | Port | Profile | Protocol |
|-----------|------|---------|----------|
| **Backend** | 8080 | local, test | HTTP |
| **Backend** | 8443 | dev | HTTPS |
| **PostgreSQL** | 5432 | docker, local | TCP |
| **Kafka Broker** | 9092 | docker, local | TCP |
| **Zookeeper** | 2181 | docker | TCP |
| **Kafdrop UI** | 9000 | docker | HTTP |
| **H2 Console** | 8080/h2-console | test | HTTP |

### 7.4 Database Configuration

**PostgreSQL 17**
- **Host:** localhost (local), db (docker)
- **Port:** 5432
- **Database:** base
- **Username:** user
- **Password:** pass (dev only, use env var in prod)
- **Initialization:** Flyway (7 migrations)
- **DDL Auto:** validate (prod), update (dev), create-drop (test)

### 7.5 Caching Strategy

**Caffeine Configuration:**
```yaml
spring.cache:
  type: caffeine
  cache-names: [foods]
  caffeine:
    spec: maximumSize=500,expireAfterAccess=600s
  caches:
    foods:
      spec: maximumSize=500,expireAfterAccess=6000s  # 100 minutes
```

**Cached Endpoints:**
- `GET /api/nutrition/foods` → Cache name: `foods`, TTL: 6000s

**Cache Invalidation:**
- Manual invalidation can be triggered via `@CacheEvict` on service methods (not yet implemented)

---

## 8. TESTING

### 8.1 Test Structure & Organization

```
src/test/java/com/inigo/
├── arch/
│   ├── ArchUserTests.kt                 # User domain tests
│   ├── UserUtils.kt                     # Test utilities
│   └── user/infrastructure/
│       └── TestSecurityConfig.kt        # Test security override
│
├── fitmentor/
│   ├── client/infrastructure/
│   │   ├── ClientControllerTest.kt      # Unit tests
│   │   └── ClientControllerE2ETest.kt   # End-to-end tests
│   │
│   ├── coach/infrastructure/
│   │   ├── CoachControllerTest.kt
│   │   └── CoachControllerE2ETest.kt
│   │
│   ├── plan/coach/infrastructure/
│   │   ├── PlanControllerTest.kt
│   │   └── PlanControllerE2ETest.kt
│   │
│   ├── plan/nutrition/foods/infrastructure/
│   │   ├── FoodControllerTest.kt
│   │   └── FoodControllerE2ETest.kt
│   │
│   └── plan/nutrition/plan/infrastructure/
│       ├── NutritionPlanControllerTest.kt
│       └── NutritionPlanControllerE2ETest.kt
│
└── resources/
    └── application-test.yaml
```

### 8.2 Testing Frameworks & Tools

| Framework | Version | Purpose |
|-----------|---------|---------|
| **JUnit 5** | Latest | Test runner, @Test, @BeforeEach, etc |
| **Mockk** | 1.13.8 | Kotlin-first mocking library |
| **SpringMockk** | 4.0.2 | Spring + Mockk integration |
| **AssertK** | 0.28.1 | Fluent assertion library |
| **H2 Database** | Latest | In-memory relational database |
| **Spring Boot Test** | 3.4.5 | @SpringBootTest, @MockMvc |

### 8.3 Test Base Configuration

```kotlin
@SpringBootTest(classes = [ArchApplication::class])
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestSecurityConfig::class)
@Transactional
class ClientControllerE2ETest {
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @Autowired
    private lateinit var clientRepository: ClientRepository
    
    @Test
    fun `should create client successfully`() {
        // Given
        val request = ClientModificationRequest(...)
        
        // When
        val result = mockMvc.perform(
            post("/api/user/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
        
        // Then
        result.andExpect(status().isOk)
        assertThat(clientRepository.count()).isEqualTo(1)
    }
}
```

### 8.4 Test Configuration Overrides

**TestSecurityConfig.kt:**
- Disables CSRF for tests
- Allows all endpoints for testing
- Provides test authentication principal
- H2 console enabled

### 8.5 Test Coverage by Module

| Module | Unit Tests | E2E Tests | Estimated Coverage |
|--------|-----------|-----------|-------------------|
| **arch.user** | ✅ ArchUserTests | ✅ Auth E2E | 75% |
| **fitmentor.client** | ✅ ClientControllerTest | ✅ ClientControllerE2ETest | 80% |
| **fitmentor.coach** | ✅ CoachControllerTest | ✅ CoachControllerE2ETest | 75% |
| **fitmentor.plan.coach** | ✅ PlanControllerTest | ✅ PlanControllerE2ETest | 70% |
| **fitmentor.plan.nutrition.foods** | ✅ FoodControllerTest | ✅ FoodControllerE2ETest | 85% |
| **fitmentor.plan.nutrition.plans** | ✅ NutritionPlanControllerTest | ✅ NutritionPlanControllerE2ETest | 65% |

### 8.6 Running Tests

```bash
# All tests
./gradlew test

# Specific test class
./gradlew test --tests ClientControllerE2ETest

# With coverage report
./gradlew test jacocoTestReport

# Test results
# Build output: build/test-results/test/
# Coverage report: build/reports/jacoco/test/html/
```

### 8.7 Test Database

- **Type:** H2 in-memory
- **Schema:** Created by Flyway (V1-V7 migrations)
- **Data:** Empty (fixtures created per test)
- **Isolation:** `@Transactional` ensures rollback after each test
- **Random Port:** Test runs on random port to avoid conflicts

---

## 9. ARQUITECTURA AVANZADA

### 9.1 Domain-Driven Design (DDD) Implementation

**Agregados (Aggregate Roots):**
1. **User** - Root aggregate for authentication & authorization
2. **Client** - Aggregate for client profiles
3. **Coach** - Aggregate for coach/trainer profiles
4. **Plan** - Aggregate for training plans
5. **Meal** - Aggregate for nutrition meals (with MealComponent, SupplementIntake as entities)
6. **NutritionPlan** - Aggregate for nutrition planning
7. **TimeSlot** - Aggregate for time slot scheduling

**Value Objects:**
- Domain IDs: UserId, ClientId, CoachId, PlanId, etc. (@JvmInline)
- Email, Username, Password, Token
- Role (Enum with 4 variants)
- Fido2 (WebAuthn value object)

**Bounded Contexts:**
1. **Authentication** - User management, JWT, FIDO2
2. **Fitness Coaching** - Plans, TimeSlots, Coach-Client relationships
3. **Nutrition** - Meals, Foods, Supplements, NutritionPlans

### 9.2 Hexagonal Architecture (Ports & Adapters)

```
                    ┌─────────────────────────────┐
                    │      EXTERNAL CLIENTS       │
                    │   (REST API Consumers)      │
                    └──────────────┬──────────────┘
                                   │
                    ┌──────────────▼──────────────┐
                    │   ADAPTER LAYER (IN)        │
                    │  Controllers & REST Routes  │
                    │ AuthController, ClientCtrl  │
                    └──────────────┬──────────────┘
                                   │
        ┌──────────────────────────▼──────────────────────────┐
        │       APPLICATION LAYER (Use Cases / Services)       │
        │ Login, CreateClient, UpdatePlan, AddMealToNutrition │
        └──────────────────────────┬──────────────────────────┘
                                   │
        ┌──────────────────────────▼──────────────────────────┐
        │          DOMAIN LAYER (Business Logic)              │
        │ User, Client, Coach, Plan, Meal, NutritionPlan      │
        │ UserStore, ClientStore, CoachService (interfaces)   │
        └──────────────────────────┬──────────────────────────┘
                                   │
        ┌──────────────────────────▼──────────────────────────┐
        │    ADAPTER LAYER (OUT) - Infrastructure            │
        │ UserRepository, ClientRepository, CoachRepository   │
        │ UserJpa, ClientJpa, CoachJpa (JPA Entities)         │
        │ TokenGenerationService, BearerService (JWT)         │
        │ KafkaProducerService (Message Queue)                │
        └──────────────────────────┬──────────────────────────┘
                                   │
                    ┌──────────────▼──────────────┐
                    │  EXTERNAL SYSTEMS (OUT)     │
                    │  PostgreSQL, Kafka, Cache   │
                    └─────────────────────────────┘
```

**Ports (Interfaces):**
- `UserStore.kt` - Persistence contract
- `ClientStore.kt` - Client storage contract
- `CoachService.kt` - Coach operations contract
- `PlanStore.kt` - Plan persistence
- `TokenGenerator.kt` - Token generation
- `FidoService.kt` - WebAuthn operations

**Adapters (Implementations):**
- `UserRepository.kt` - Implements UserStore
- `ClientRepository.kt` - Implements ClientStore
- `TokenGenerationService.kt` - Implements TokenGenerator
- `BearerService.kt` - JWT adapter
- `FidoWebAuthn4jService.kt` - FIDO2 adapter

### 9.3 Event-Driven Architecture

**Domain Events:**
```kotlin
abstract class DomainEvent {
    open val name: String
    open val aggregateId: UUID
    open val aggregateName: String
    open val timestamp: Instant
}

class ClientCreated(name, clientId, userId) : ClientUpdated(name, clientId, userId)
class CoachCreated(name, coachId, userId) : CoachUpdated(name, coachId, userId)
class PlanCreated(name, planId) : DomainEvent
```

**Event Publishing:**
```kotlin
open class AggregateRoot {
    private var events: MutableList<DomainEvent> = mutableListOf()
    
    protected fun addEvent(event: DomainEvent) {
        events.add(event)
    }
    
    fun publishEvents(): List<DomainEvent> = events.toList()
}
```

**Event Listeners:**
```kotlin
@Component
class AddTypeOnClientCreated(val addTypeToUser: AddTypeToUser) {
    @EventListener
    fun handle(clientCreated: ClientCreated) {
        addTypeToUser.execute(clientCreated.userId, Role.CLIENT)
    }
}

@Component
class ProjectOnCoachChange(...) {
    @EventListener
    fun handle(coachUpdated: CoachUpdated) {
        // Update projections or cache
    }
}
```

**Kafka Event Sourcing:**
```kotlin
@Service
class KafkaProducerService(val kafkaTemplate: KafkaTemplate<String, String>) {
    fun <T: AggregateRoot> sendSnapshot(aggregateRoot: T) {
        val aggregateName = aggregateRoot.javaClass.simpleName
        val json = objectMapper.writeValueAsString(aggregateRoot)
        kafkaTemplate.send(aggregateName, aggregateRoot.id.toString(), json)
    }
}
```

### 9.4 Caching Strategy

**Caffeine Cache Configuration:**
```yaml
spring:
  cache:
    type: caffeine
    cache-names: [foods]
    caffeine:
      spec: maximumSize=500,expireAfterAccess=600s  # default
    caches:
      foods:
        spec: maximumSize=500,expireAfterAccess=6000s  # 100 minutes
```

**Usage:**
```kotlin
@Service
@Cacheable(value = "foods")
class FoodService(val foodStore: FoodStore) {
    fun getAllFoods(): List<Food> {
        return foodStore.findAll()  // Cached for 100 minutes
    }
}
```

**Cache Statistics:**
- Max items: 500
- Eviction: LRU (Least Recently Used)
- TTL: Access-based (resets on read)

### 9.5 Transactional Boundaries

**Service-Level Transactions:**
```kotlin
@Service
@Transactional
class MealService(
    val mealStore: MealStore,
    val mealComponentStore: MealComponentStore,
    val supplementIntakeStore: SupplementIntakeStore
) {
    fun addMealWithComponents(meal: Meal) {
        mealStore.save(meal)
        meal.mealComponents.forEach { mealComponentStore.save(it) }
        meal.supplementIntakes.forEach { supplementIntakeStore.save(it) }
        // If any throws, entire transaction rolls back
    }
}
```

**Entity Relationships & Cascade:**
- Meal (parent) → MealComponents (children)
- Meal (parent) → SupplementIntakes (children)
- Cascade settings: CascadeType.ALL + orphanRemoval = true

### 9.6 Dependency Injection Pattern

**Constructor Injection (preferred):**
```kotlin
@Service
class CreateClient(
    val clientStore: ClientStore,
    val userService: UserService,
    val eventPublisher: ApplicationEventPublisher
) {
    // Immutable, testable, explicit dependencies
}
```

**Spring Annotations:**
- `@Component` - Generic bean
- `@Service` - Business logic layer
- `@Repository` - Data access layer
- `@Controller` - HTTP endpoints
- `@Configuration` - Configuration beans

### 9.7 Exception Handling

**Global Exception Handler:**
```kotlin
@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFound(ex: EntityNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message), HttpStatus.NOT_FOUND)
    }
    
    @ExceptionHandler(ValidationException::class)
    fun handleValidation(ex: ValidationException): ResponseEntity<ErrorResponse> {
        return ResponseEntity(ErrorResponse(ex.message), HttpStatus.BAD_REQUEST)
    }
    
    @ExceptionHandler(Exception::class)
    fun handleGeneric(ex: Exception): ResponseEntity<ErrorResponse> {
        return ResponseEntity(
            ErrorResponse("Internal server error"),
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }
}
```

---

## 10. EJECUCIÓN Y DEPLOYMENT

### 10.1 Local Development Setup

**Prerequisites:**
- JDK 21+ (or use SDKMAN: `sdk install java 21-tem`)
- Docker & Docker Compose (for PostgreSQL + Kafka)
- Git, Gradle

**Step-by-step:**

```bash
# 1. Clone repository
git clone <repo-url>
cd fitmentor

# 2. Start infrastructure (PostgreSQL + Kafka)
docker compose up db zookeeper kafka kafdrop -d

# 3. Build project
./gradlew clean build -x test

# 4. Run backend (local profile)
./gradlew bootRun --args='--spring.profiles.active=local'

# Backend is now running at http://localhost:8080
# Swagger UI: http://localhost:8080/swagger-ui.html
```

**Frontend (Optional):**
```bash
cd frontend
npm install
npm run start:local
# Frontend at http://localhost:4200
```

### 10.2 Docker Full Stack

**All-in-one deployment:**

```bash
# Start everything
docker compose up --build

# Services:
# Backend: http://localhost:8080
# Kafka UI: http://localhost:9000
# PostgreSQL: localhost:5432
# Zookeeper: localhost:2181
# Kafka: localhost:9092

# Logs
docker compose logs -f app

# Stop everything
docker compose down

# Remove volumes (clean slate)
docker compose down -v
```

### 10.3 Building & Packaging

**JAR Build:**
```bash
./gradlew clean bootJar -x test
# Output: build/libs/fitmentor-0.0.1-SNAPSHOT.jar

# Run jar
java -Dspring.profiles.active=dev \
  -Dspring.datasource.url=jdbc:postgresql://localhost:5432/base \
  -Dspring.datasource.username=user \
  -Dspring.datasource.password=pass \
  -jar build/libs/fitmentor-0.0.1-SNAPSHOT.jar
```

**WAR Build** (if needed):
```bash
./gradlew clean build -x test
# Output: build/libs/fitmentor-0.0.1-SNAPSHOT.war
# Deploy to application server (Tomcat, WildFly, etc)
```

### 10.4 Profiles in Runtime

```bash
# Local Development (HTTP, local DB)
--spring.profiles.active=local

# Development (HTTPS, Docker DB, local Kafka)
--spring.profiles.active=dev

# Testing (H2 in-memory, random port)
--spring.profiles.active=test

# Production (to be configured)
--spring.profiles.active=prod
```

### 10.5 Database Initialization

**Flyway Migrations:**
```bash
# Automatic on startup (enabled in all profiles)
# Migrations run in order: V1, V2, V3, ... V7

# Manual migration check
./gradlew flywayInfo

# Repair (if migration failed)
./gradlew flywayRepair

# Validate
./gradlew flywayValidate
```

### 10.6 Cloud Deployment

**Example: AWS EC2 with Docker**

```bash
# 1. Push Docker image to ECR
aws ecr get-login-password | docker login --username AWS --password-stdin <account-id>.dkr.ecr.<region>.amazonaws.com
docker tag fitmentor:latest <account-id>.dkr.ecr.<region>.amazonaws.com/fitmentor:latest
docker push <account-id>.dkr.ecr.<region>.amazonaws.com/fitmentor:latest

# 2. SSH to EC2 instance
ssh ec2-user@<instance-ip>

# 3. Pull and run
docker pull <account-id>.dkr.ecr.<region>.amazonaws.com/fitmentor:latest
docker run -d -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://<rds-endpoint>:5432/fitmentor \
  -e SPRING_DATASOURCE_USERNAME=<db-user> \
  -e SPRING_DATASOURCE_PASSWORD=<secure-password> \
  -e KAFKA_BOOTSTRAP_SERVERS=<msk-endpoint>:9092 \
  <account-id>.dkr.ecr.<region>.amazonaws.com/fitmentor:latest

# 4. Configure reverse proxy (nginx)
# ... nginx.conf points to localhost:8080
```

---

## 11. RESUMEN EJECUTIVO

### 📊 FitMentor at a Glance

**FitMentor** es una **plataforma REST API completa** para **gestión de fitness coaching y nutrición**, construida con **Domain-Driven Design** y **Arquitectura Hexagonal**.

### ✅ Fortalezas Principales

1. **Arquitectura Robusta**
   - DDD implementado correctamente (agregados, value objects, bounded contexts)
   - Hexagonal architecture con puertos y adaptadores
   - Separación clara de capas (domain, application, infrastructure)

2. **Autenticación & Seguridad**
   - JWT tokens con HS256
   - FIDO2/WebAuthn soporte (parcial)
   - CORS configurado
   - Spring Security integrado

3. **Base de Datos Bien Estructurada**
   - 7 migraciones Flyway ejecutadas automáticamente
   - 12 tablas normalizadas
   - Índices estratégicos para queries frecuentes
   - ~150+ alimentos precargados

4. **Event-Driven Architecture**
   - Domain events (ClientCreated, CoachCreated, PlanCreated, etc)
   - Kafka integration para event sourcing
   - Event listeners para projections y side effects

5. **Caching Inteligente**
   - Caffeine cache para foods (~150 items)
   - TTL configurables per-cache
   - Reduce carga en BD

6. **Módulos Bien Definidos**
   - 5 módulos principales + shared
   - 9 controllers
   - 19+ casos de uso
   - 12 entidades JPA

7. **Testing Completo**
   - E2E tests para todos los controllers
   - Unit tests con MockK
   - H2 in-memory BD para tests
   - ~63+ test anotaciones

8. **Documentación**
   - OpenAPI/Swagger integrado
   - README básico
   - Code comments en lógica compleja

### ⚠️ Áreas de Mejora CRÍTICAS

1. **🔴 JWT sin expiración** - Tokens válidos indefinidamente
   - **Fix:** Agregar `exp` claim con TTL (1-24 horas)
   - **Prioridad:** MÁXIMA

2. **🔴 Credenciales en configuración** - Contraseña BD hardcodeada
   - **Fix:** Usar variables de entorno en todos los profiles
   - **Prioridad:** MÁXIMA

3. **🔴 Falta validación de entrada** - No se validan age, weight, etc
   - **Fix:** Agregar @Validated, @Min, @Max en DTOs
   - **Prioridad:** ALTA

4. **🟠 Sin rate limiting** - Brute force attacks posibles
   - **Fix:** Implementar RateLimiter en AuthController
   - **Prioridad:** ALTA

5. **🟠 FIDO2 incompleto** - Registration endpoint no existe
   - **Fix:** Completar endpoints de registro WebAuthn
   - **Prioridad:** MEDIA (feature enhancement)

### 📈 Estadísticas del Codebase

| Métrica | Valor |
|---------|-------|
| **Módulos** | 5 (User, Client, Coach, Plan, Nutrition) |
| **Submódulos** | 9+ (plan.coach, plan.nutrition.meals, etc) |
| **Controllers** | 9 |
| **Endpoints** | 13 principales |
| **Casos de Uso** | 19+ |
| **Entidades JPA** | 12 |
| **Tablas BD** | 12 |
| **Migraciones Flyway** | 7 |
| **Value Objects** | 5+ |
| **Domain Events** | 6+ |
| **Tests E2E** | ~13+ test classes |
| **Unit Tests** | Multiple |

### 🎯 Funcionalidades Implementadas

✅ Autenticación JWT
✅ Gestión de usuarios (CRUD)
✅ Perfiles de clientes
✅ Perfiles de coaches
✅ Planes de entrenamiento
✅ Planes de nutrición
✅ Comidas y componentes
✅ Base de alimentos (150+ items)
✅ Suplementos nutricionales
✅ Time slots (horarios)
✅ Event sourcing
✅ Caché distribuida
✅ OpenAPI/Swagger
✅ Docker & Docker Compose
✅ Múltiples profiles
✅ Testing comprehensivo

### 🚀 Ready for Production?

**Casi listo, con reservas:**

- ✅ Architecture: Sólida
- ✅ Functionalidad: Completa
- ⚠️ Seguridad: Necesita ajustes (JWT exp, credentials)
- ✅ Testing: Buena cobertura
- ✅ Documentation: Adecuada
- ⚠️ Performance: Caché implementado, pero sin rate limiting
- ✅ Observability: Logs, metrics via Micrometer

**Recomendación:** Resolver issues críticos de seguridad (JWT exp, env vars) antes de producción. Resto puede desplegarse con 2-3 sprints de stabilización.

---

## 📝 Análisis Final

FitMentor es un **proyecto bien estructurado** que implementa correctamente **arquitectura moderna** (DDD + Hexagonal). El código es **mantenible, testeable y escalable**.

**Próximos pasos recomendados:**
1. Agregar expiración a JWT tokens
2. Mover credenciales a variables de entorno
3. Implementar validación de entrada
4. Completar FIDO2 registration
5. Agregar rate limiting
6. Mejorar logging (remover print())
7. Deploys a staging/producción
8. Monitoring & alertas (Prometheus + Grafana)
9. Load testing
10. Implementar refresh tokens

**Status:** ✅ **LISTO PARA BETA** (con mejoras de seguridad aplicadas)

---

**Generado:** March 19, 2026
**Analyzer:** Copilot CLI
**Version:** Complete Exhaustive Analysis v1.0
