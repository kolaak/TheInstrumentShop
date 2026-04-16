# Technical Debt Summary

## Overview

The Java Instrument Shop codebase carries significant technical debt across multiple categories. The most impactful issues stem from running **four different Spring Boot major/minor versions** across the microservices, with three modules on end-of-life versions. This fragmentation increases maintenance burden, creates inconsistency in API contracts, and blocks the adoption of modern Java features.

## Debt by Category

### 1. EOL/Deprecated Runtimes and Frameworks — Severity: High

| Component | Module | Current Version | Status |
|-----------|--------|----------------|--------|
| Spring Boot | shop | 1.5.19.RELEASE | EOL since Aug 2019 |
| Spring Cloud | shop | Dalston.SR5 | EOL |
| Netflix Hystrix | shop | (managed by Spring Cloud) | Maintenance mode / deprecated |
| Spring Boot | stock | 2.1.3.RELEASE | EOL since late 2019 |
| Java target | shop, stock | 1.8 | Approaching/at end of public updates |

### 2. Outdated Runtime/Production Dependencies — Severity: Medium

| Component | Module | Current Version | Issue |
|-----------|--------|----------------|-------|
| Log4j | shop | 2.6.1 | Critical CVEs including Log4Shell |
| Spring Boot | instruments | 2.7.5 | Approaching EOL |
| OTel annotations | annotator | 1.19.1-alpha | Pre-release alpha |
| OTel annotations | instruments, shop | 1.19.2-alpha | Pre-release alpha |
| JUnit | root POM | 3.8.1 | Extremely outdated |
| Cucumber | stock | 1.2.5 (info.cukes) | Deprecated group ID |

### 3. Code Quality and Architectural Issues — Severity: Low

| Issue | Module | Location |
|-------|--------|----------|
| SQL injection vulnerability | instruments | `FindInstrumentRepositoryImpl.findInstrumentByID()` |
| Cartesian product query | instruments | `FindInstrumentRepositoryImpl.findInstruments()` |
| Thread.sleep anti-patterns | products, conductors | `ProductFilterService` (30+ occurrences) |
| Hardcoded magic number 999 | products, conductors | `getMyInt()` and `myCoolFunction234234234()` |
| Empty catch blocks | products, conductors | Throughout `ProductFilterService` |
| Endpoint typo `/insruments` | stock | `StockResource.java` |
| Location hardcoded to "Oregon" | conductors | `ConductorsController.java` |
| Public fields in model classes | shop | `Instrument.java` |
| Mixed Spring Boot versions | all | 1.5.x, 2.1.x, 2.7.x, 3.2.x |

## Impact Assessment

- **Security Risk**: The Log4j 2.6.1 dependency and SQL injection vulnerability pose immediate security concerns
- **Operational Risk**: EOL Spring Boot versions receive no security patches or bug fixes
- **Development Velocity**: Mixed framework versions require developers to maintain expertise across multiple Spring Boot generations
- **Testing Complexity**: Different test frameworks (JUnit 3.8.1, Cucumber 1.2.5) make unified testing difficult

## Related Documents

- [Outdated Components (detailed)](outdated-components.md)
- [Maintenance Burden](maintenance-burden.md)
- [Remediation Plan](remediation-plan.md)
- [Root-level Technical Debt Report](../technical-debt-report.md)

---

[← Back to README](../README.md)
