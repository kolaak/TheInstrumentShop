# Code Metrics

## Overall Codebase Statistics

| Metric | Value |
|--------|-------|
| Total Java source files | 60 |
| Total lines of Java code | ~4,805 |
| Maven modules | 7 |
| Spring Boot services | 5 (shop, products, conductors, stock, instruments) |
| Standalone tools | 1 (annotator) |
| REST endpoints | 14 |
| JPA entities | 3 (Instrument, Stock in instruments; Stock in stock) |

## Lines of Code by Module

| Module | Java Files | Approx. LOC | Complexity |
|--------|-----------|-------------|------------|
| shop | 16 | ~1,200 | Medium — service orchestration, Hystrix, exercises |
| products | 5 | ~700 | High — ProductFilterService dominates |
| conductors | 7 | ~650 | High — duplicate ProductFilterService |
| instruments | 12 | ~550 | Medium — JPA, custom queries |
| stock | 9 | ~350 | Low — simple CRUD |
| annotator | 4 | ~300 | Medium — AST manipulation |
| test | 1 | ~55 | Low — traffic generator |

## Complexity Hotspots

### 1. ProductFilterService (products module) — ~600 lines
- **Cyclomatic complexity**: Low per method, but 30+ methods with identical structure
- **Issue**: Extreme code duplication — each `myCoolFunction*()` follows the same Thread.sleep pattern
- **Root cause**: Intentional design for APM training (needle in haystack exercise)

### 2. HomeController (shop module) — ~130 lines
- **Concerns**: Mixes web presentation, exercise scoring, permission checking, and latency tracking
- **Issue**: Violates Single Responsibility Principle

### 3. Exercises (shop module) — ~200 lines
- **Cyclomatic complexity**: Medium — large switch statement with 15 cases
- **Issue**: Complex validation logic tightly coupled to HomeController

### 4. OpenTelemetryAnnotator (annotator module) — ~130 lines
- **Complexity**: Medium — file I/O, AST manipulation, property management
- **Issue**: Method exclusion logic hardcoded (skip main, setters, getters, health checks)

## Duplication Analysis

| Duplicated Component | Module A | Module B | Notes |
|---------------------|----------|----------|-------|
| ProductFilterService | products | conductors | Near-identical ~600 line classes |
| Product model | products | conductors | Identical POJO |
| ProductService / ConductorsService | products | conductors | Same in-memory DAO |
| InvalidLocaleException | shop, products, conductors, instruments | — | 4 identical copies |
| FilteredProducts / FilteredInstrument | conductors | instruments | Near-identical locale filter |

## Related Documents

- [Dependency Analysis](dependency-analysis.md) | [Security Patterns](security-patterns.md) | [Tech Debt](tech-debt.md)
- [Architecture → Patterns](../architecture/patterns.md)

---

[← Back to README](../README.md)
