# Remediation Plan

## Prioritized Action Items

Issues are prioritized by severity and impact. No time estimates are provided — only qualitative effort descriptors.

---

### Priority 1: Critical Security Fixes — Severity: High

#### 1.1 Upgrade Log4j 2.6.1 in `shop` module
- **Current**: Log4j 2.6.1 (log4j-api, log4j-core)
- **Target**: Latest Log4j 2.x or migrate to SLF4J/Logback
- **Effort**: Low complexity — dependency version update, verify logging configuration
- **Risk if deferred**: Active exploitation of Log4Shell CVE-2021-44228
- **AWS Transformation**: Use `AWS/early-access-log4j-to-slf4j-migration` to migrate to SLF4J

#### 1.2 Fix SQL injection in `instruments` module
- **File**: `FindInstrumentRepositoryImpl.findInstrumentByID()`
- **Current**: `"FROM instruments i WHERE i.ID = " + id.toString()`
- **Fix**: Use parameterized queries (`setParameter()`) or Spring Data JPA derived query methods
- **Effort**: Low complexity — single method change

---

### Priority 2: EOL Runtime Upgrades — Severity: High

#### 2.1 Upgrade `shop` module from Spring Boot 1.5.x to 3.x
- **Current**: Spring Boot 1.5.19.RELEASE, Java 1.8, Spring Cloud Dalston, Hystrix
- **Target**: Spring Boot 3.2.x, Java 17+
- **Effort**: High complexity — most complex module due to Spring Cloud dependencies
- **Key changes required**:
  - Migrate from Java 8 to Java 17+
  - Migrate Spring Cloud Dalston → latest Spring Cloud release
  - Replace Hystrix with Resilience4j
  - Migrate `javax.*` → `jakarta.*` namespace
  - Update RestTemplate usage (or migrate to WebClient)
  - Update Thymeleaf configuration (remove LEGACYHTML5 mode)
  - Remove NekoHTML dependency (no longer needed with modern Thymeleaf)
- **AWS Transformation**: Use `AWS/java-version-upgrade` to upgrade Java and Spring Boot

#### 2.2 Upgrade `stock` module from Spring Boot 2.1.x to 3.x
- **Current**: Spring Boot 2.1.3.RELEASE, Java 1.8
- **Target**: Spring Boot 3.2.x, Java 17+
- **Effort**: Medium complexity — simpler module with fewer dependencies
- **Key changes required**:
  - Migrate from Java 8 to Java 17+
  - Migrate `javax.persistence.*` → `jakarta.persistence.*`
  - Migrate `javax.annotation.*` → `jakarta.annotation.*`
  - Migrate `javax.validation.*` → `jakarta.validation.*`
  - Update Cucumber from `info.cukes:1.2.5` → `io.cucumber:7.x+`
  - Update JUnit dependency
- **AWS Transformation**: Use `AWS/java-version-upgrade` to upgrade Java and Spring Boot

#### 2.3 Upgrade `instruments` module from Spring Boot 2.7.x to 3.x
- **Current**: Spring Boot 2.7.5, Java 17
- **Target**: Spring Boot 3.2.x
- **Effort**: Medium complexity — Java version is already 17, focus on namespace migration
- **Key changes required**:
  - Migrate `javax.persistence.*` → `jakarta.persistence.*`
  - Migrate `javax.annotation.*` → `jakarta.annotation.*`
  - Update OpenTelemetry annotations from alpha to stable release
  - Remove duplicate `spring-boot-starter-web` declaration
- **AWS Transformation**: Use `AWS/java-version-upgrade` to upgrade Spring Boot

---

### Priority 3: Outdated Dependency Updates — Severity: Medium

#### 3.1 Update OpenTelemetry annotations across modules
- **Current**: 1.19.1-alpha (annotator), 1.19.2-alpha (instruments, shop)
- **Target**: Latest stable 2.x release
- **Effort**: Low complexity — dependency version bump, verify annotation compatibility

#### 3.2 Migrate JUnit from 3.8.1 to 5.x
- **Current**: JUnit 3.8.1 in root POM
- **Target**: JUnit 5.10.x+ (Jupiter)
- **Effort**: Medium complexity — test code refactoring required

#### 3.3 Migrate Cucumber in `stock` module
- **Current**: `info.cukes:cucumber-java:1.2.5`
- **Target**: `io.cucumber:cucumber-java:7.x+`
- **Effort**: Medium complexity — group ID change, API updates

---

### Priority 4: Code Quality Improvements — Severity: Low

#### 4.1 Refactor ProductFilterService
- Eliminate repetitive `myCoolFunction*()` methods
- Replace `Thread.sleep()` with proper async patterns or remove if intentional latency is no longer needed
- Replace empty catch blocks with proper error handling
- Remove or extract magic number `999`

#### 4.2 Fix Cartesian product query
- **File**: `FindInstrumentRepositoryImpl.findInstruments()`
- Add proper JOIN conditions or use separate queries for each table

#### 4.3 Fix endpoint and naming issues
- Correct `/insruments` → `/instruments` in `StockResource.java`
- Remove hardcoded `"Oregon"` override in `ConductorsController.java`
- Fix typo `/instrumemnts` in `StockRepo.java`

#### 4.4 Encapsulate public fields
- Change public fields in `shop` module's `Instrument.java` to private with getters/setters

---

## Recommended Migration Order

1. **stock** — Simplest module, fewest dependencies, good starting point
2. **instruments** — Already on Java 17, primarily namespace migration
3. **shop** — Most complex due to Spring Cloud, Hystrix, Thymeleaf, and multiple service integrations

See [Migration → Component Order](../migration/component-order.md) for detailed migration sequencing.

---

## Related Documents

- [Summary](summary.md)
- [Outdated Components](outdated-components.md)
- [Maintenance Burden](maintenance-burden.md)
- [Root-level Technical Debt Report](../technical-debt-report.md)

---

[← Back to README](../README.md)
