# Component Migration Order

## Recommended Migration Sequence

Modules should be upgraded in order of increasing complexity and dependency count. Each module can be upgraded independently since there are no compile-time inter-module dependencies.

### Phase 1: `stock` Module (Simplest)

**Current**: Spring Boot 2.1.3.RELEASE / Java 1.8
**Target**: Spring Boot 3.2.x / Java 17+

**Rationale**: Fewest dependencies, no downstream service calls, simple CRUD operations. Low risk, high confidence.

**Key Migration Steps**:
1. Update Java version from 1.8 to 17
2. Update Spring Boot from 2.1.3 to 3.2.x
3. Migrate `javax.persistence` → `jakarta.persistence`
4. Migrate `javax.annotation` → `jakarta.annotation`
5. Update Cucumber from `info.cukes:1.2.5` → `io.cucumber:7.x+`
6. Update versions-maven-plugin
7. Verify H2 database compatibility
8. Test all endpoints: `/legacy`, `/insruments`, `/healthcheck`

---

### Phase 2: `instruments` Module

**Current**: Spring Boot 2.7.5 / Java 17
**Target**: Spring Boot 3.2.x / Java 17 (or 21)

**Rationale**: Already on Java 17, primarily namespace migration. Medium complexity due to JPA/PostgreSQL integration.

**Key Migration Steps**:
1. Update Spring Boot from 2.7.5 to 3.2.x
2. Migrate `javax.persistence` → `jakarta.persistence` (all entity classes, repositories)
3. Migrate `javax.annotation` → `jakarta.annotation` (PostConstruct)
4. Update OpenTelemetry annotations from 1.19.2-alpha to stable 2.x
5. Remove duplicate spring-boot-starter-web declaration
6. Address SQL injection in `FindInstrumentRepositoryImpl`
7. Verify PostgreSQL driver compatibility
8. Test all endpoints: `/instruments`, `/stocks`, `/healthcheck`

---

### Phase 3: `shop` Module (Most Complex)

**Current**: Spring Boot 1.5.19.RELEASE / Java 1.8 / Spring Cloud Dalston / Hystrix
**Target**: Spring Boot 3.2.x / Java 17+ / Resilience4j

**Rationale**: Highest complexity — spans 3 major Spring Boot versions, requires Spring Cloud migration, Hystrix replacement, and Thymeleaf configuration updates. Depends on all other services being upgraded first to ensure compatibility.

**Key Migration Steps**:
1. Upgrade Java from 1.8 to 17
2. Upgrade Spring Boot from 1.5.x through 2.x to 3.2.x (may need intermediate steps)
3. Replace Spring Cloud Dalston with current Spring Cloud release
4. Replace Hystrix with Resilience4j:
   - `@HystrixCommand` → `@CircuitBreaker` or `@Retry`
   - Update fallback method patterns
5. Migrate `javax.*` → `jakarta.*` namespace
6. Update Thymeleaf configuration (remove LEGACYHTML5 mode, remove NekoHTML)
7. Upgrade Log4j 2.6.1 to latest (or migrate to SLF4J)
8. Update AspectJ and OpenTelemetry annotations
9. Verify RestTemplate compatibility for all downstream calls
10. Test all endpoints and inter-service communication

---

### Phase 4 (Optional): Modernize `products` and `conductors`

**Current**: Spring Boot 3.2.2 / Java 17 (already current)

**Steps**:
- Refactor ProductFilterService (remove Thread.sleep patterns if no longer needed for training)
- Fix hardcoded Oregon location in ConductorsController
- Consider upgrading to Java 21

## Related Documents

- [Technical Debt → Remediation Plan](../technical-debt/remediation-plan.md)
- [Test Specifications](test-specifications.md) | [Validation Criteria](validation-criteria.md)

---

[← Back to README](../README.md)
