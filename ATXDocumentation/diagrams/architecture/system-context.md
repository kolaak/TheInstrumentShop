# System Context Diagram

## Deployment Architecture

```mermaid
C4Context
    title Java Instrument Shop - System Context

    Person(user, "Shop User", "Browses instruments and products")

    System_Boundary(docker, "Docker Compose Environment") {
        System(shop, "Shop Service", "Spring Boot 1.5.19 / Java 8<br/>Port 8010 — Frontend + API Gateway")
        System(products, "Products Service", "Spring Boot 3.2.2 / Java 17<br/>Port 8020 — Product Catalog")
        System(conductors, "Conductors Service", "Spring Boot 3.2.2 / Java 17<br/>Port 8050 — Utah Products")
        System(stock, "Stock Service", "Spring Boot 2.1.3 / Java 8<br/>Port 8030 — Inventory (H2)")
        System(instruments, "Instruments Service", "Spring Boot 2.7.5 / Java 17<br/>Port 8040 — Instrument CRUD")
        SystemDb(postgres, "PostgreSQL 13.1", "Port 5432 — Instrument Data")
        System(redis, "Redis", "Caching Layer")
        System(tester, "Traffic Tester", "Load Generator")
    }

    Rel(user, shop, "HTTP :8010")
    Rel(shop, products, "REST /products")
    Rel(shop, conductors, "REST /conductors")
    Rel(shop, stock, "REST /legacy")
    Rel(shop, instruments, "REST /instruments")
    Rel(instruments, postgres, "JDBC")
    Rel(tester, shop, "HTTP traffic")
```

## ASCII System Context

```
                    ┌──────────────┐
                    │  👤 User     │
                    │  Browser     │
                    └──────┬───────┘
                           │ HTTP :8010
                           ▼
┌─────────────────────────────────────────────────────────┐
│              Docker Compose Environment                   │
│                                                          │
│  ┌──────────────────────────────────────────────┐       │
│  │           Shop Service (API Gateway)          │       │
│  │        Spring Boot 1.5.19 | Java 8            │       │
│  │     Hystrix Circuit Breaker | Thymeleaf       │       │
│  └───┬──────────┬────────────┬──────────┬───────┘       │
│      │          │            │          │               │
│      ▼          ▼            ▼          ▼               │
│  ┌────────┐ ┌────────┐ ┌────────┐ ┌──────────┐        │
│  │Products│ │Conduct-│ │ Stock  │ │Instrumen-│        │
│  │Service │ │ors Svc │ │Service │ │ts Service│        │
│  │SB 3.2.2│ │SB 3.2.2│ │SB 2.1.3│ │SB 2.7.5 │        │
│  │Java 17 │ │Java 17 │ │Java 8  │ │Java 17   │        │
│  │In-mem  │ │In-mem  │ │H2 DB   │ │JPA/JDBC  │        │
│  └────────┘ └────────┘ └────────┘ └────┬─────┘        │
│                                         │               │
│                                         ▼               │
│                                    ┌──────────┐         │
│  ┌──────┐                          │PostgreSQL│         │
│  │Redis │                          │  13.1    │         │
│  │Cache │                          │ :5432    │         │
│  └──────┘                          └──────────┘         │
│                                                          │
│  ┌──────────┐  ┌──────────┐                             │
│  │Annotator │  │  Tester  │                             │
│  │(CLI tool)│  │(traffic) │                             │
│  └──────────┘  └──────────┘                             │
└─────────────────────────────────────────────────────────┘
```

## Network Configuration

- All services share the `instrument_shop` Docker network (external)
- Services reference each other by Docker service names (DNS resolution)
- Shop's `application.properties` defines service URLs:
  - `productsUri = http://products:8020`
  - `conductorsUri = http://conductors:8050`
  - `stockUri = http://stock:8030`
  - `instrumentsUri = http://instruments:8040`

## Related Documents

- [Component Diagrams](../structural/component-diagrams.md)
- [Sequence Diagrams](../behavioral/sequence-diagrams.md)
- [Architecture → System Overview](../../architecture/system-overview.md)
- [Deployment Configuration](../../specialized/deployment-configuration.md)

---

[← Back to README](../../README.md)
