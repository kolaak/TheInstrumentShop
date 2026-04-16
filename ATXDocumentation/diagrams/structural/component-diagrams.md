# Component Diagrams

## Service Relationship Diagram

```mermaid
graph TD
    User[👤 User Browser] --> Shop

    subgraph Docker Network: instrument_shop
        Shop[🛒 Shop<br/>:8010<br/>Spring Boot 1.5.19]
        Products[📦 Products<br/>:8020<br/>Spring Boot 3.2.2]
        Conductors[🎵 Conductors<br/>:8050<br/>Spring Boot 3.2.2]
        Stock[📊 Stock<br/>:8030<br/>Spring Boot 2.1.3]
        Instruments[🎸 Instruments<br/>:8040<br/>Spring Boot 2.7.5]
        Postgres[(🐘 PostgreSQL<br/>:5432)]
        Redis[(Redis)]
        Tester[🧪 Tester]
    end

    Shop -->|GET /products| Products
    Shop -->|GET /conductors<br/>Utah only| Conductors
    Shop -->|GET /legacy| Stock
    Shop -->|GET /instruments| Instruments
    Instruments -->|JDBC| Postgres
    Tester -->|HTTP traffic| Shop
```

## ASCII Component Diagram

```
┌──────────────────────────────────────────────────────────────┐
│                  Docker Network: instrument_shop              │
│                                                              │
│  ┌─────────┐    ┌──────────┐    ┌────────────┐              │
│  │  Redis   │    │  Tester  │───▶│    Shop    │              │
│  │ (cache)  │    │          │    │  :8010     │              │
│  └─────────┘    └──────────┘    │  SB 1.5.19 │              │
│                                  └──┬──┬──┬──┘              │
│                     ┌───────────────┘  │  │  └──────┐       │
│                     ▼                  ▼  │         ▼       │
│              ┌──────────┐    ┌────────┐│  │  ┌───────────┐  │
│              │ Products │    │ Stock  ││  │  │Instruments│  │
│              │  :8020   │    │ :8030  ││  │  │  :8040    │  │
│              │ SB 3.2.2 │    │SB 2.1.3││  │  │ SB 2.7.5 │  │
│              └──────────┘    │  [H2]  ││  │  └─────┬─────┘  │
│                              └────────┘│  │        │        │
│              ┌──────────┐              │  │        ▼        │
│              │Conductors│◀─────────────┘  │  ┌──────────┐   │
│              │  :8050   │  (Utah only)    │  │PostgreSQL│   │
│              │ SB 3.2.2 │                 │  │  :5432   │   │
│              └──────────┘                 │  └──────────┘   │
│                                           │                  │
│  ┌──────────┐                             │                  │
│  │Annotator │ (standalone CLI tool)       │                  │
│  │ Java 17  │                             │                  │
│  └──────────┘                             │                  │
└──────────────────────────────────────────────────────────────┘
```

## Class Dependency Diagram (Shop Module)

```mermaid
classDiagram
    JavaShopApp --> HomeController
    JavaShopApp --> RestTemplate

    HomeController --> ProductService
    HomeController --> InstrumentService
    HomeController --> Exercises
    HomeController --> PropertiesUpdater
    HomeController --> User

    ProductService --> ProductRepo
    ProductService --> StockRepo
    ProductService --> ProductDTO
    ProductService --> StockDTO

    InstrumentService --> InstrumentRepo
    InstrumentService --> InstrumentDTO

    ProductRepo --> RestTemplate
    StockRepo --> RestTemplate
    InstrumentRepo --> RestTemplate
```

## Related Documents

- [Architecture → System Overview](../architecture/system-overview.md)
- [Sequence Diagrams](../diagrams/behavioral/sequence-diagrams.md)

---

[← Back to README](../../README.md)
