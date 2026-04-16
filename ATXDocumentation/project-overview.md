# Project Overview

## Project Identity

- **Name**: Java Instrument Shop ("InstrumenT-ation Shop")
- **Repository**: Multi-module Maven project
- **Purpose**: Microservices application for managing and displaying musical instrument inventory across multiple locations, with integrated APM training exercises

## What This Application Does

The Java Instrument Shop is a web application that allows users to browse musical instruments and products available for sale across various locations. The system serves as both a functional e-commerce prototype and an **APM (Application Performance Monitoring) training platform**, with intentional performance bugs and code patterns designed for hands-on observability exercises.

### Key Locations
- **California** (default) — Standard product and instrument retrieval
- **Colorado** — Intentional ~1 second latency for APM training
- **Utah** — Routes to alternate product provider (conductors service)
- **Chicago** — Triggers expensive Cartesian product database query
- **Oregon** — Disabled locale, throws exceptions
- **Sri Lanka** — Instrument data sourced from Sri Lankan marketplace

## Technology Stack

| Component | Technology | Version(s) |
|-----------|-----------|------------|
| Language | Java | 1.8, 17 |
| Framework | Spring Boot | 1.5.19, 2.1.3, 2.7.5, 3.2.2 |
| Cloud | Spring Cloud | Dalston.SR5 |
| Database | PostgreSQL | 13.1-alpine |
| In-Memory DB | H2 | (managed by Spring Boot) |
| Cache | Redis | (Docker image) |
| Monitoring | OpenTelemetry | 1.19.x-alpha, 2.2.0 |
| Build | Maven | Multi-module |
| Deployment | Docker Compose | Version 3 |
| Template | Thymeleaf | (managed by Spring Boot 1.5.x) |

## Module Overview

| Module | Port | Spring Boot | Role |
|--------|------|-------------|------|
| shop | 8010 | 1.5.19 | Frontend + API gateway |
| products | 8020 | 3.2.2 | Product catalog |
| conductors | 8050 | 3.2.2 | Utah-specific products |
| stock | 8030 | 2.1.3 | Inventory management |
| instruments | 8040 | 2.7.5 | Instrument CRUD (PostgreSQL) |
| annotator | — | N/A | OTel annotation CLI tool |
| test | — | N/A | Traffic generator |

## Key Findings

### Critical Issues
1. **3 modules on EOL Spring Boot versions** (1.5.x, 2.1.x, 2.7.x)
2. **Log4j 2.6.1 vulnerability** (CVE-2021-44228 / Log4Shell)
3. **SQL injection** in instruments module
4. **Deprecated Hystrix** circuit breaker (maintenance mode)

### Architectural Observations
- Services communicate via synchronous HTTP REST (no message queues)
- No authentication or authorization implemented
- Mixed Java versions (8 and 17) across modules
- Significant code duplication between products and conductors modules

### AWS Transformation Recommendation
See [Technical Debt Report](technical-debt-report.md) for recommended AWS-managed transformations: **AWS/java-version-upgrade** and **AWS/early-access-log4j-to-slf4j-migration**.

## Related Documents

- [README (Navigation)](README.md)
- [Architecture → System Overview](architecture/system-overview.md)
- [Technical Debt Report](technical-debt-report.md)

---

[← Back to README](README.md)
