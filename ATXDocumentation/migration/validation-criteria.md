# Validation Criteria

## Success Criteria for Migration

### Per-Module Validation

Each module upgrade is considered successful when:

1. **Build Success**: Module compiles without errors using `mvn clean package`
2. **Health Check**: `/healthcheck` endpoint returns HTTP 200
3. **Functional Equivalence**: All REST endpoints return the same response structure as pre-migration
4. **No javax References**: No remaining `javax.persistence`, `javax.annotation`, or `javax.validation` imports (for modules migrating to Spring Boot 3.x)
5. **No Deprecated APIs**: No usage of deprecated Spring Boot APIs
6. **Docker Deployment**: Service starts and passes health check in Docker Compose environment

### Stock Module Criteria

- [ ] Spring Boot 3.2.x parent declared in pom.xml
- [ ] Java 17 (or higher) target in pom.xml
- [ ] All `javax.*` imports replaced with `jakarta.*`
- [ ] H2 database functions correctly (5 seed records created)
- [ ] `/legacy` endpoint returns 5 stock records
- [ ] `/insruments` endpoint returns stock records
- [ ] `/healthcheck` returns HTTP 200
- [ ] Cucumber tests migrated to `io.cucumber` group ID

### Instruments Module Criteria

- [ ] Spring Boot 3.2.x parent declared in pom.xml
- [ ] All `javax.persistence.*` replaced with `jakarta.persistence.*`
- [ ] All `javax.annotation.*` replaced with `jakarta.annotation.*`
- [ ] PostgreSQL connection functions correctly
- [ ] `/instruments?location=California` returns instrument data
- [ ] `/instruments?location=Chicago` returns data (Cartesian product behavior preserved or fixed)
- [ ] `/instruments?location=Oregon` returns empty array
- [ ] `/stocks` endpoint returns stock data
- [ ] OpenTelemetry annotations upgraded to stable version

### Shop Module Criteria

- [ ] Spring Boot 3.2.x parent declared in pom.xml
- [ ] Java 17+ target in pom.xml
- [ ] Spring Cloud updated to current release
- [ ] Hystrix replaced with Resilience4j (fallback behavior preserved)
- [ ] All `javax.*` imports replaced with `jakarta.*`
- [ ] Log4j upgraded to safe version (or migrated to SLF4J)
- [ ] Thymeleaf renders correctly without LEGACYHTML5 mode
- [ ] All downstream service calls function correctly
- [ ] `/` renders homepage with products and instruments
- [ ] Utah routing to conductors service works
- [ ] Exercise scoring system functions correctly

### System-Wide Criteria

- [ ] All 5 services start successfully in Docker Compose
- [ ] All health checks pass within startup period
- [ ] Inter-service communication works across all services
- [ ] No Spring Boot version older than 3.x remains in any module
- [ ] No Java version older than 17 remains in any module
- [ ] No known critical CVE vulnerabilities in dependencies

## Related Documents

- [Component Order](component-order.md) | [Test Specifications](test-specifications.md)
- [Technical Debt → Remediation Plan](../technical-debt/remediation-plan.md)

---

[← Back to README](../README.md)
