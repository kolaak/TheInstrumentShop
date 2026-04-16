# Architectural Patterns

## Identified Patterns

### 1. MVC (Model-View-Controller)
- **Where**: shop module (Thymeleaf views + HomeController), products, conductors, instruments, stock modules (REST controllers)
- **Implementation**: Spring MVC with `@Controller` (shop) and `@RestController` (all other services)
- **View layer**: Thymeleaf templates in shop; JSON responses in other services

### 2. Repository Pattern
- **Where**: All data-access modules
- **Implementation**:
  - `InstrumentRepository extends JpaRepository<Instrument, String>` (instruments)
  - `StockRepository extends CrudRepository<Stock, String>` (stock, instruments)
  - `FindInstrumentRepositoryImpl` — custom repository with native SQL queries (instruments)
  - `ProductRepo`, `StockRepo`, `InstrumentRepo` — HTTP-based "repositories" in shop module

### 3. Service Layer Pattern
- **Where**: All modules
- **Implementation**: `@Service`-annotated classes encapsulate business logic
  - `ProductService`, `InstrumentService` (shop) — orchestration services
  - `ProductService` (products), `ConductorsService` (conductors) — in-memory DAOs
  - `InstrumentService`, `InstrumentStocksService` (instruments) — JPA-backed services
  - `StockService`, `InstrumentStocksService` (stock) — CrudRepository-backed services

### 4. Circuit Breaker Pattern (Hystrix)
- **Where**: shop module
- **Implementation**: `@HystrixCommand(fallbackMethod = "...")` annotations on repository methods
  - `InstrumentRepo.getinstrumentDTOs()` → fallback: `instrumentsNotFound()` (returns empty map)
  - `StockRepo.getStockDTOs()` → fallback: `stocksNotFound()` (returns empty map)
  - `StockRepo.getInstrumentStockDTOs()` → fallback: `stocksNotFound()` (returns empty map)

### 5. DTO (Data Transfer Object) Pattern
- **Where**: shop module
- **Implementation**: Separate DTO classes for inter-service communication
  - `ProductDTO` — received from products/conductors service
  - `InstrumentDTO` — received from instruments service
  - `StockDTO` — received from stock service
  - DTOs are merged with stock data to create domain `Product` and `Instrument` objects

### 6. Builder Pattern
- **Where**: shop module `Instrument` model
- **Implementation**: `buildIt()` and `buildForLocale()` methods return `this` for fluent construction
  - `buildForLocale()` includes locale validation (isEnglish check)
  - `buildIt()` is a simpler builder without locale validation

### 7. Singleton Pattern
- **Where**: shop module
- **Implementation**:
  - `Exercises.s_instance` — eager singleton for exercise scoring
  - `PropertiesUpdater.s_instance` — eager singleton for score persistence

### 8. Visitor Pattern
- **Where**: annotator module
- **Implementation**: JavaParser's `VoidVisitorAdapter<Void>` used in `OtelAnnotationVisitor` to traverse AST and add `@WithSpan` / `@SpanAttribute` annotations to method declarations

### 9. API Gateway Pattern (Informal)
- **Where**: shop module
- **Implementation**: Shop service acts as a frontend aggregator, routing requests to appropriate backend services based on location parameter. Not a formal API gateway but functions as one.

## Anti-Patterns Identified

| Anti-Pattern | Location | Description |
|-------------|----------|-------------|
| God Class | `ProductFilterService` (products, conductors) | ~600 lines with 30+ nearly identical methods |
| Magic Numbers | `ProductFilterService` | `999` used as latency trigger; `966` as sleep base |
| Silent Exception Swallowing | `ProductFilterService` | Empty `catch(Exception e){}` blocks throughout |
| String Concatenation in Queries | `FindInstrumentRepositoryImpl` | SQL/HQL injection vulnerability |
| Static Mutable State | `HomeController` | `s_coloradoLatency`, `s_utahLatency` static fields |
| Hardcoded Configuration | `ConductorsController` | Location overridden to `"Oregon"` |

## Related Documents

- [System Overview](system-overview.md) | [Components](components.md) | [Dependencies](dependencies.md)
- [Code Metrics](../analysis/code-metrics.md)

---

[← Back to README](../README.md)
