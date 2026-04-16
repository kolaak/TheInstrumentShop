# System Overview

## Project Identity

- **Name**: Java Instrument Shop ("InstrumenT-ation Shop")
- **Group ID**: `com.splunk` (root), `com.shabushabu.javashop` (modules)
- **Type**: Multi-module Maven project with microservices architecture
- **Codebase Size**: ~4,805 lines of Java across 60 files, 7 Maven modules

## Technology Stack

| Layer | Technology | Details |
|-------|-----------|---------|
| **Language** | Java | 1.8 (shop, stock), 17 (products, conductors, instruments, annotator) |
| **Framework** | Spring Boot | 1.5.19 / 2.1.3 / 2.7.5 / 3.2.2 (mixed versions) |
| **Cloud** | Spring Cloud Dalston | shop module only (Hystrix, Eureka starters) |
| **Database** | PostgreSQL 13.1 | instruments data (instruments_for_sale, instruments_for_sale_chicago) |
| **In-Memory DB** | H2 | stock module (embedded, auto-generated data) |
| **Cache** | Redis | Defined in docker-compose.yml |
| **Build** | Maven | Multi-module POM structure |
| **Containerization** | Docker / Docker Compose | All services containerized |
| **Monitoring** | OpenTelemetry | Instrumentation annotations across modules |
| **Template Engine** | Thymeleaf | shop module (LEGACYHTML5 mode) |

## Deployment Architecture

All services are deployed as Docker containers within a shared Docker network (`instrument_shop`). Services communicate via HTTP REST using Docker service names for DNS resolution.

### Service Ports

| Service | Port | Description |
|---------|------|-------------|
| shop | 8010 | Frontend + API gateway |
| products | 8020 | Product catalog |
| stock | 8030 | Stock management |
| instruments | 8040 | Instrument CRUD (JPA/PostgreSQL) |
| conductors | 8050 | Filtered products (Utah-specific) |
| postgresDB | 5432 | PostgreSQL database |
| redis | (default) | Caching layer |

## Key Architectural Decisions

1. **Microservices Architecture**: Each business domain is a separate Spring Boot application
2. **Docker-based Service Discovery**: Services reference each other by Docker Compose service names (no Eureka registry despite dependency)
3. **Synchronous HTTP Communication**: All inter-service calls are synchronous REST via RestTemplate
4. **Circuit Breaker Pattern**: Hystrix used in shop module for fault tolerance on stock and instrument calls
5. **Database per Service**: instruments uses PostgreSQL, stock uses H2 — each has its own data store

## Related Documents

- [Components](components.md) | [Dependencies](dependencies.md) | [Patterns](patterns.md)
- [Deployment Configuration](../specialized/deployment-configuration.md)
- [Technical Debt Report](../technical-debt-report.md)

---

[← Back to README](../README.md)
