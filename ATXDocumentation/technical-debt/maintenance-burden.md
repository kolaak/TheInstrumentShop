# Maintenance Burden Analysis

## Overview

The Java Instrument Shop project carries a significant maintenance burden due to framework fragmentation, deprecated libraries, and code quality hotspots. This document identifies the areas requiring the most maintenance attention.

---

## 1. Mixed Spring Boot Versions — Severity: High

**Impact**: The project runs four different Spring Boot versions across its modules:

| Module | Spring Boot Version | Java Version | Spring Boot Generation |
|--------|-------------------|--------------|----------------------|
| shop | 1.5.19.RELEASE | 1.8 | 1st Gen (EOL) |
| stock | 2.1.3.RELEASE | 1.8 | 2nd Gen (EOL) |
| instruments | 2.7.5 | 17 | 2nd Gen (approaching EOL) |
| products | 3.2.2 | 17 | 3rd Gen (current) |
| conductors | 3.2.2 | 17 | 3rd Gen (current) |

**Maintenance cost**: Developers must maintain expertise across three different Spring Boot generations. Dependency upgrades, security patches, and configuration changes differ significantly between versions. Shared patterns and libraries cannot be unified.

---

## 2. Deprecated Library Dependencies — Severity: High

### Netflix Hystrix (shop module)
- Hystrix entered maintenance mode in 2018 and is no longer actively developed
- No new features, security patches, or bug fixes
- The Spring Cloud Hystrix starter was removed in newer Spring Cloud releases
- Replacement: Resilience4j or Spring Cloud Circuit Breaker

### Spring Cloud Dalston (shop module)
- Dalston is several major releases behind current Spring Cloud
- Incompatible with Spring Boot 2.x and above
- Blocks the shop module from being upgraded independently

### Cucumber info.cukes (stock module)
- The `info.cukes` group ID has been deprecated and migrated to `io.cucumber`
- Version 1.2.5 lacks modern Cucumber features and has known issues
- The community has fully moved to `io.cucumber` 7.x+

---

## 3. Code Quality Hotspots — Severity: Medium

### ProductFilterService (products and conductors modules)
- **~600 lines** of highly repetitive code with 30+ `myCoolFunction*()` methods
- Each method follows an identical pattern: declare `sleepy` variable → `Thread.sleep()` → empty `catch` block
- Contains intentional latency injection for Colorado location via `myCoolFunction234234234()` with magic number `999`
- Significant maintenance burden: any change requires understanding the intentional vs. unintentional behavior

### FindInstrumentRepositoryImpl (instruments module)
- **SQL injection vulnerability** in `findInstrumentByID()`: concatenates user input directly into HQL query
- **Cartesian product query** in `findInstruments()`: `SELECT * FROM instruments_for_sale, instruments_for_sale_chicago` produces a cross join with no WHERE clause
- Both methods represent data access anti-patterns that are difficult to maintain safely

### HomeController (shop module)
- Contains commented-out HTTP call with hardcoded external URL (Lambda function)
- Mixes concerns: user management, exercise scoring, permission checking, latency tracking all in one controller
- Uses static mutable fields (`s_coloradoLatency`, `s_utahLatency`) for cross-request state

---

## 4. Configuration and Naming Issues — Severity: Low

| Issue | Module | File | Impact |
|-------|--------|------|--------|
| Endpoint typo `/insruments` | stock | `StockResource.java` | API consumers must use misspelled URL |
| Location hardcoded to "Oregon" | conductors | `ConductorsController.java` | Incoming `location` parameter is ignored |
| Typo `/instrumemnts` | shop | `StockRepo.java` | Calls misspelled endpoint on stock service |
| Duplicate spring-boot-starter-web | instruments | `pom.xml` | Declared twice (once with exclusion, once without) |
| Redundant logger lines | instruments | `InstrumentsApplication.java` | Same log message repeated 6 times |

---

## 5. Testing Infrastructure — Severity: Medium

- Root POM depends on **JUnit 3.8.1**, which is 3 major versions behind
- Only one test file exists: `shop/src/test/java/.../ShopTest.java`
- Cucumber tests in stock module use deprecated `info.cukes` dependency
- No integration tests for inter-service HTTP communication
- No test coverage for the annotator module

---

## Related Documents

- [Summary](summary.md)
- [Outdated Components](outdated-components.md)
- [Remediation Plan](remediation-plan.md)
- [Root-level Technical Debt Report](../technical-debt-report.md)

---

[← Back to README](../README.md)
