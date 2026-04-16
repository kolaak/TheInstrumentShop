# Dependency Analysis

## Internal Dependencies (Runtime HTTP)

```
shop ──HTTP──→ products     (GET /products?location=...)
shop ──HTTP──→ conductors   (GET /conductors?location=..., Utah only)
shop ──HTTP──→ stock        (GET /legacy, GET /instrumemnts)
shop ──HTTP──→ instruments  (GET /instruments?location=...)
instruments ──JDBC──→ PostgreSQL (port 5432)
```

## External Dependency Map

### Framework Dependencies
| Library | shop | stock | instruments | products | conductors | annotator |
|---------|------|-------|-------------|----------|------------|-----------|
| Spring Boot 1.5.x | ✅ | — | — | — | — | — |
| Spring Boot 2.1.x | — | ✅ | — | — | — | — |
| Spring Boot 2.7.x | — | — | ✅ | — | — | — |
| Spring Boot 3.2.x | — | — | — | ✅ | ✅ | — |
| Spring Cloud Dalston | ✅ | — | — | — | — | — |
| Spring Data JPA | — | ✅ | ✅ | — | — | — |

### Runtime Dependencies
| Library | shop | stock | instruments | products | conductors | annotator |
|---------|------|-------|-------------|----------|------------|-----------|
| Log4j 2.6.1 | ✅ | — | — | — | — | — |
| Log4j2 (via starter) | — | — | ✅ | — | — | — |
| OTel annotations | ✅ (1.19.2α) | — | ✅ (1.19.2α) | ✅ (2.2.0) | — | ✅ (1.19.1α) |
| AspectJ 1.9.19 | ✅ | — | — | — | — | — |
| Hystrix | ✅ | — | — | — | — | — |
| PostgreSQL driver | — | — | ✅ | — | — | — |
| H2 database | — | ✅ | — | — | — | — |
| NekoHTML 1.9.22 | ✅ | — | — | — | — | — |
| JavaParser 3.23.1 | — | — | — | — | — | ✅ |
| Thymeleaf | ✅ | — | — | — | — | — |

### Test Dependencies
| Library | shop | stock | instruments | products | conductors | annotator |
|---------|------|-------|-------------|----------|------------|-----------|
| JUnit 3.8.1 | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| Cucumber 1.2.5 | — | ✅ | — | — | — | — |
| Hamcrest | — | ✅ (2.1) | ✅ (managed) | — | — | — |
| spring-boot-starter-test | — | ✅ | ✅ | — | — | — |

## Dependency Health Assessment

| Status | Count | Examples |
|--------|-------|---------|
| 🔴 EOL/Critical | 4 | Spring Boot 1.5.x, 2.1.x; Spring Cloud Dalston; Hystrix |
| 🟡 Outdated | 6 | Log4j 2.6.1, Spring Boot 2.7.x, JUnit 3.8.1, OTel alpha, Cucumber 1.2.5 |
| 🟢 Current/Acceptable | 5+ | Spring Boot 3.2.2, OTel 2.2.0, PostgreSQL driver (managed) |

## Related Documents

- [Architecture → Dependencies](../architecture/dependencies.md)
- [Technical Debt → Outdated Components](../technical-debt/outdated-components.md)
- [Code Metrics](code-metrics.md) | [Security Patterns](security-patterns.md)

---

[← Back to README](../README.md)
