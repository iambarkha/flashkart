FlashKart — Complete Project Blueprint

🏗️ Overall Architecture
[Client / Browser / Mobile]
|
▼
[API Gateway :8080]
(routing + JWT filter)
|
┌─────────────────────┼──────────────────────┐
▼                     ▼                       ▼
[user-service :8081]  [product-service :8082]  [order-service :8083]
|                     |                       |
▼                     ▼                       ▼
[Postgres DB]         [Postgres DB]           [Postgres DB]
(users)               (products)               (orders)

                            [sale-service :8084]
                                    |
                              [Postgres DB]
                               (flash sales)

         All services publish/consume events via [Apache Kafka]
                                    |
                        [notification-service :8085]
                         (listens to Kafka, sends alerts)

         All services containerized via [Docker Compose]

📦 Services Overview
ServicePortResponsibilityapi-gateway8080Routing, JWT validation, rate limitinguser-service8081Auth, registration, user managementproduct-service8082Product catalog, inventorysale-service8084Flash sale creation, scheduling, time windowsorder-service8083Place orders, concurrency control, payment statusnotification-service8085Kafka consumer, email/push notifications

📁 Package Structure (user-service as reference)
src/main/java/com/flashkart/userservice/
├── UserServiceApplication.java
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── AuthController.java
│   └── UserController.java
├── service/
│   ├── UserService.java
│   ├── JwtService.java
│   └── impl/
│       ├── UserServiceImpl.java
│       └── JwtServiceImpl.java
├── repository/
│   └── UserRepository.java
├── entity/
│   ├── User.java
│   └── Role.java
├── dto/
│   ├── request/
│   │   ├── RegisterRequest.java
│   │   └── LoginRequest.java
│   └── response/
│       ├── UserResponse.java
│       ├── AuthResponse.java
│       └── ApiResponse.java
├── mapper/
│   └── UserMapper.java
├── security/
│   ├── JwtAuthFilter.java
│   └── UserDetailsServiceImpl.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── UserAlreadyExistsException.java
│   ├── UserNotFoundException.java
│   └── InvalidCredentialsException.java
├── validation/
│   └── StrongPassword.java
└── aop/
└── LoggingAspect.java

🗂️ All Tasks

TASK 1 — user-service: Foundation & Auth
Goal: Registration, Login, JWT, Roles, User profile APIs
Java 21 Features:

Records for all DTOs
Sealed classes for ApiResponse<T> variants (stretch)
Pattern matching in instanceof checks
var keyword where type is obvious

Spring Modules:

Spring Security — custom SecurityFilterChain
Spring Data JPA — UserRepository
Spring Validation — @Valid, custom @StrongPassword
Spring Web — REST controllers

Must-Have APIs:

POST /api/v1/auth/register
POST /api/v1/auth/login
GET /api/v1/users/me
GET /api/v1/users (ADMIN only)
PUT /api/v1/users/me

Security:

JWT filter extending OncePerRequestFilter
JwtService — generate, validate, extract claims
Password encoded with BCryptPasswordEncoder
Role-based access using @PreAuthorize

Exception Handling:

@RestControllerAdvice global handler
UserAlreadyExistsException
UserNotFoundException
InvalidCredentialsException
Never expose stack trace in response

Coding Standards:

Controller has zero business logic
Service is interface + impl
Entity uses @Getter @Setter @Builder not @Data
UUIDs as primary keys
@PrePersist / @PreUpdate for timestamps
All configs in application.yml, nothing hardcoded

Testing:

Testcontainers with real Postgres
Test register happy path
Test duplicate email throws exception
Test login returns JWT
Test unauthorized access to protected route


TASK 2 — user-service: AOP & Actuator
Goal: Cross-cutting concerns — logging, performance monitoring
Spring Modules:

Spring AOP — @Aspect, @Around, @AfterThrowing
Spring Actuator — custom health indicator

AOP Requirements:

LoggingAspect — log method entry/exit for all service methods
PerformanceAspect — log execution time for methods taking >500ms
SecurityAuditAspect — log every login attempt, never log password

Actuator:

Expose health, info, metrics
Custom HealthIndicator for DB connectivity
App info in /actuator/info

Testing:

Test AOP advice fires on service call
Test custom health indicator


TASK 3 — product-service: Setup & Catalog
Goal: Product CRUD, categories, search
Java 21 Features:

Records for DTOs
Pattern matching for product type checks
SequencedCollection where applicable

Spring Modules:

Spring Data JPA with Specification for dynamic filtering
Spring Cache (@Cacheable, @CacheEvict) with Redis
Spring Validation

Must-Have APIs:

POST /api/v1/products (ADMIN)
GET /api/v1/products (with filters)
GET /api/v1/products/{id}
PUT /api/v1/products/{id} (ADMIN)
DELETE /api/v1/products/{id} (ADMIN)

Exception Handling:

ProductNotFoundException
InsufficientStockException
DuplicateProductException

Coding Standards:

MapStruct mapper
Pagination on list endpoints
Optimistic locking on inventory (@Version)

Testing:

Test product creation
Test filtering and pagination
Test cache is hit on second call


TASK 4 — product-service: Inventory Management
Goal: Thread-safe stock reservation and release
Java 21 Features:

Virtual threads awareness with DB pool

Spring Modules:

@Transactional with correct isolation levels
Pessimistic locking (PESSIMISTIC_WRITE)
Custom JPA queries

Requirements:

reserveStock(productId, quantity) — atomic, thread-safe
releaseStock(productId, quantity) — on order cancel
Stock never goes below zero

Testing:

100 concurrent threads, only 10 succeed for 10 items
Stock release on cancellation


TASK 5 — sale-service: Flash Sale Scheduling
Goal: Create flash sales, enforce time windows, auto-start/end
Java 21 Features:

Records for DTOs
Sealed classes for SaleStatus state transitions

Spring Modules:

@Scheduled — auto-activate and expire sales
Spring Data JPA
@Transactional

Must-Have APIs:

POST /api/v1/sales (ADMIN)
GET /api/v1/sales/active (public)
GET /api/v1/sales/{id}
PUT /api/v1/sales/{id}/cancel (ADMIN)

Sale States:

SCHEDULED → ACTIVE → EXPIRED / CANCELLED
Scheduler runs every minute
Product cannot be in two active sales simultaneously

Exception Handling:

SaleNotFoundException
SaleNotActiveException
ProductAlreadyInActiveSaleException


TASK 6 — order-service: Place Order with Concurrency Control
Goal: Handle thousands of simultaneous order requests safely
Java 21 Features:

Virtual threads — configure Tomcat
Structured concurrency (StructuredTaskScope)
Records for DTOs

Spring Modules:

@Transactional with correct propagation
WebClient to call product-service and sale-service
Spring Data JPA

Must-Have APIs:

POST /api/v1/orders
GET /api/v1/orders/{id}
GET /api/v1/orders/my-orders
PUT /api/v1/orders/{id}/cancel

Concurrency Requirements:

One user cannot buy same flash sale product twice
DB SELECT FOR UPDATE for distributed locking
Idempotent order placement

Kafka Events:

order.placed
order.cancelled
stock.reserve.request

Exception Handling:

SaleNotActiveException
DuplicateOrderException
StockUnavailableException
OrderNotFoundException


TASK 7 — Kafka Integration
Goal: Async communication between services
Kafka Concepts:

Topics, partitions, consumer groups
Producer with KafkaTemplate
Consumer with @KafkaListener
Dead letter topic
Retry with @RetryableTopic

Requirements:

order-service publishes OrderPlacedEvent to order-events topic
product-service consumes and confirms stock reservation
Retry 3 times on failure then dead letter topic
Events serialized as JSON using Records


TASK 8 — notification-service: Kafka Consumer
Goal: Listen to events, simulate notifications
Requirements:

order.placed → log order confirmation
sale.starting → log sale notification
order.cancelled → log cancellation
Demonstrate consumer group behavior


TASK 9 — api-gateway
Goal: Single entry point, JWT validation, routing
Spring Modules:

Spring Cloud Gateway (reactive)
Custom GlobalFilter for JWT
Route config in application.yml

Routes:

/api/v1/users/** → user-service
/api/v1/products/** → product-service
/api/v1/orders/** → order-service
/api/v1/sales/** → sale-service

Public Routes:

/api/v1/auth/**
GET /api/v1/products/**
GET /api/v1/sales/active

Security:

401 for missing/invalid token
403 for insufficient role


TASK 10 — Docker Compose: Full Stack
Goal: One command brings up everything
Requirements:

Multi-stage Dockerfile per service
docker-compose.yml with all 6 services
Postgres per service + Kafka + Zookeeper
Health checks on all containers
Services wait for DB to be healthy
Named volumes for data persistence
All secrets via environment variables


🚀 Execution Order
TASK 1 → TASK 2 → TASK 3 → TASK 4 → TASK 5
→ TASK 6 → TASK 7 → TASK 8 → TASK 9 → TASK 10

✅ Progress Tracker
TaskStatusTASK 1 — user-service Foundation & Auth🔄 In ProgressTASK 2 — user-service AOP & Actuator⏳ PendingTASK 3 — product-service Catalog⏳ PendingTASK 4 — product-service Inventory⏳ PendingTASK 5 — sale-service Scheduling⏳ PendingTASK 6 — order-service Concurrency⏳ PendingTASK 7 — Kafka Integration⏳ PendingTASK 8 — notification-service⏳ PendingTASK 9 — api-gateway⏳ PendingTASK 10 — Docker Compose⏳ Pending

prompt to put in new chat:
"I am working on FlashKart project — a flash sale platform. We are on Task 1 of 10 (user-service). The package is com.flashkart.userservice. Tech stack is Java 21, Spring Boot 4, PostgreSQL, MapStruct, JJWT 0.12.6, Testcontainers. Approved files so far: Role.java, User.java, all DTOs, UserRepository.java (in progress). Here is the next file to review: [paste URL]"