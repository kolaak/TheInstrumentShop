# Dependencies

## Internal Service Dependency Graph

```
shop (8010)
├── → products (8020)    [HTTP GET /products?location=...]
├── → conductors (8050)  [HTTP GET /conductors?location=... (Utah only)]
├── → stock (8030)       [HTTP GET /legacy, /instrumemnts]
└── → instruments (8040) [HTTP GET /instruments?location=...]

instruments (8040)
└── → postgresDB (5432)  [JDBC postgresql://postgresDB:5432/instruments]

conductors (8050)
└── (no downstream service calls)

products (8020)
└── (no downstream service calls)

stock (8030)
└── (no downstream service calls, uses embedded H2)
```

## External Library Dependencies by Module

### shop
| Dependency | Version | Scope |
|-----------|---------|-------|
| spring-boot-starter-parent | 1.5.19.RELEASE | parent |
| spring-cloud-dependencies | Dalston.SR5 | BOM |
| spring-boot-starter-thymeleaf | (managed) | compile |
| spring-boot-starter-actuator | (managed) | compile |
| spring-cloud-starter-hystrix | (managed) | compile |
| spring-cloud-starter-eureka | (managed) | compile |
| aspectjweaver | 1.9.19 | compile |
| log4j-api | 2.6.1 | compile |
| log4j-core | 2.6.1 | compile |
| opentelemetry-instrumentation-annotations | 1.19.2-alpha | compile |
| nekohtml | 1.9.22 | compile |

### stock
| Dependency | Version | Scope |
|-----------|---------|-------|
| spring-boot-starter-parent | 2.1.3.RELEASE | parent |
| spring-boot-starter-web | (managed) | compile |
| spring-boot-starter-data-jpa | (managed) | compile |
| spring-boot-starter-actuator | (managed) | compile |
| h2 | (managed) | compile |
| spring-boot-starter-test | (managed) | test |
| hamcrest-core | 2.1 | compile |
| cucumber-java | 1.2.5 | compile |
| cucumber-junit | 1.2.5 | compile |
| cucumber-spring | 1.2.5 | compile |

### instruments
| Dependency | Version | Scope |
|-----------|---------|-------|
| spring-boot-starter-parent | 2.7.5 | parent |
| spring-boot-starter-web | (managed) | compile |
| spring-boot-starter-data-jpa | (managed) | compile |
| spring-boot-starter-actuator | (managed) | compile |
| spring-boot-starter-log4j2 | (managed) | compile |
| spring-boot-starter-test | (managed) | test |
| postgresql | (managed) | runtime |
| hamcrest-core | (managed) | compile |
| opentelemetry-instrumentation-annotations | 1.19.2-alpha | compile |

### products
| Dependency | Version | Scope |
|-----------|---------|-------|
| spring-boot-starter-parent | 3.2.2 | parent |
| spring-boot-starter-web | (managed) | compile |
| spring-boot-docker-compose | (managed) | runtime/optional |
| opentelemetry-instrumentation-annotations | 2.2.0 | compile |

### conductors
| Dependency | Version | Scope |
|-----------|---------|-------|
| spring-boot-starter-parent | 3.2.2 | parent |
| spring-boot-starter-web | (managed) | compile |
| spring-boot-docker-compose | (managed) | runtime/optional |

### annotator
| Dependency | Version | Scope |
|-----------|---------|-------|
| javaparser-core | 3.23.1 | compile |
| opentelemetry-instrumentation-annotations | 1.19.1-alpha | compile |
| exec-maven-plugin | 3.1.0 | build |

### root POM
| Dependency | Version | Scope |
|-----------|---------|-------|
| junit | 3.8.1 | test |

## Related Documents

- [System Overview](system-overview.md) | [Components](components.md) | [Patterns](patterns.md)
- [Dependency Analysis](../analysis/dependency-analysis.md)
- [Outdated Components](../technical-debt/outdated-components.md)

---

[← Back to README](../README.md)
