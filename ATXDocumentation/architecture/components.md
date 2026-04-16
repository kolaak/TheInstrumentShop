# Components

## Service Components

### 1. Shop Service (`shop` module) — Port 8010

**Role**: Frontend web application and API gateway. Serves the Thymeleaf-based UI and orchestrates calls to downstream services.

**Key Classes**:
- `JavaShopApp` — Spring Boot application entry point, enables Hystrix, provides RestTemplate bean
- `HomeController` — Main MVC controller handling `/`, `/score`, `/healthcheck` endpoints
- `ProductResource` — REST controller at `/products` (proxies to ProductService)
- `ProductService` — Aggregates product data from ProductRepo and stock data from StockRepo
- `InstrumentService` — Retrieves instruments from InstrumentRepo with locale filtering
- `ProductRepo` — HTTP client for products/conductors services with location-based routing
- `StockRepo` — HTTP client for stock service with Hystrix fallback
- `InstrumentRepo` — HTTP client for instruments service with Hystrix fallback
- `Exercises` — Exercise scoring and validation system for APM training
- `PropertiesUpdater` — File-based properties management for scores
- `User` — Simple POJO for user name and location

**Spring Boot**: 1.5.19.RELEASE | **Java**: 1.8

---

### 2. Products Service (`products` module) — Port 8020

**Role**: Product catalog service providing a list of available products with location-based filtering.

**Key Classes**:
- `ProductServiceApplication` — Spring Boot entry point
- `ProductController` — REST controller at `/products` with location parameter
- `ProductService` — In-memory product DAO with hardcoded data (5 products)
- `ProductFilterService` — Complex filtering pipeline with intentional latency (Thread.sleep patterns)
- `Product` — Product model (id, name, description, price)

**Spring Boot**: 3.2.2 | **Java**: 17

---

### 3. Conductors Service (`conductors` module) — Port 8050

**Role**: Alternate product provider for Utah location. Mirrors products service but with Oregon-specific filtering logic.

**Key Classes**:
- `ConductorsServiceApplication` — Spring Boot entry point
- `ConductorsController` — REST controller at `/conductors` (hardcodes location to "Oregon")
- `ConductorsService` — In-memory product DAO (same 5 products as ProductService)
- `ProductFilterService` — Mirror of products' filter service (all filtering commented out)
- `FilteredProducts` — Oregon locale validation (throws InvalidLocaleException if Oregon is disabled)
- `Product` — Product model

**Spring Boot**: 3.2.2 | **Java**: 17

---

### 4. Stock Service (`stock` module) — Port 8030

**Role**: Stock/inventory management service using H2 in-memory database.

**Key Classes**:
- `StockManagerApplication` — Spring Boot entry point
- `StockResource` — REST controller with `/legacy`, `/insruments` (typo), `/healthcheck` endpoints
- `StockService` — Retrieves all stock records via StockRepository
- `InstrumentStocksService` — Retrieves instrument-specific stock records
- `DataGenerator` — Generates 5 synthetic stock records on startup
- `StockRepository` — Spring Data CrudRepository for Stock entity
- `InstrumentStocksRepository` — Spring Data CrudRepository for Stock entity
- `Stock` — JPA entity (productId, sku, amountAvailable)

**Spring Boot**: 2.1.3.RELEASE | **Java**: 1.8

---

### 5. Instruments Service (`instruments` module) — Port 8040

**Role**: Musical instrument CRUD service backed by PostgreSQL database.

**Key Classes**:
- `InstrumentsApplication` — Spring Boot entry point with startup logging
- `InstrumentResource` — REST controller with `/instruments`, `/stocks`, `/healthcheck` endpoints
- `InstrumentService` — Business logic with location filtering (Chicago triggers Cartesian product query)
- `InstrumentStocksService` — Retrieves instrument stock records
- `InstrumentRepository` — JPA repository extending `JpaRepository` and `FindInstrumentRepository`
- `FindInstrumentRepository` — Custom repository interface for native queries
- `FindInstrumentRepositoryImpl` — Custom repository with SQL injection vulnerability
- `Instrument` — JPA entity mapped to `instruments_for_sale` table
- `Stock` — JPA entity mapped to `InstrumentStocks` table
- `FilteredInstrument` — Oregon locale validation

**Spring Boot**: 2.7.5 | **Java**: 17

---

### 6. Annotator Tool (`annotator` module)

**Role**: Standalone CLI tool that automatically adds OpenTelemetry `@WithSpan` and `@SpanAttribute` annotations to Java source files.

**Key Classes**:
- `OpenTelemetryAnnotator` — Main class that parses Java files using JavaParser and adds OTel annotations
- `DirExplorer` — Recursive directory file walker
- `DirExplorerInstrumented` — OTel-annotated version of DirExplorer (demonstrates annotation output)
- `TestGeneratedInstrumentation` — Lists classes in a directory using JavaParser

**Build tool**: Maven with exec-maven-plugin | **Java**: 17

---

### 7. Test Module (`test` module)

**Role**: Traffic generation for testing.

**Key Classes**:
- `GenerateTraffic` — Sends HTTP requests to shop service for load testing

---

## Related Documents

- [System Overview](system-overview.md) | [Dependencies](dependencies.md) | [Patterns](patterns.md)
- [Program Structure](../reference/program-structure.md)

---

[← Back to README](../README.md)
