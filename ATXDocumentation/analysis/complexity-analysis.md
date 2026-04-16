# Complexity Analysis

## Module Complexity Ranking

| Rank | Module | Complexity | Factors |
|------|--------|-----------|---------|
| 1 | shop | **High** | Multi-service orchestration, Hystrix, Thymeleaf, exercises, latency tracking |
| 2 | products | **Medium-High** | ProductFilterService with 30+ methods, intentional latency bugs |
| 3 | conductors | **Medium-High** | Duplicate ProductFilterService, Oregon locale override |
| 4 | instruments | **Medium** | JPA/PostgreSQL integration, custom repository, locale filtering |
| 5 | annotator | **Medium** | AST manipulation with JavaParser, file I/O |
| 6 | stock | **Low** | Simple CRUD with CrudRepository, seed data |
| 7 | test | **Low** | Traffic generation only |

## Cyclomatic Complexity Highlights

### High Complexity Methods
- `Exercises.checkExercise()` — 15-branch switch statement
- `HomeController.getProductsAllLocations()` — multiple conditionals for location, user defaults, latency tracking
- `InstrumentService.getInstruments()` — locale filter + Chicago-specific path + fallback logic

### Code Duplication Complexity
- `ProductFilterService` exists in both products and conductors with near-identical code (~600 lines each)
- `InvalidLocaleException` duplicated across 4 modules
- `Product` model duplicated across 3 modules (shop, products, conductors)

## Related Documents

- [Code Metrics](code-metrics.md)
- [Architecture → Patterns](../architecture/patterns.md)

---

[← Back to README](../README.md)
