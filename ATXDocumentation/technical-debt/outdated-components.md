# Outdated Components — Detailed Analysis

## Module-by-Module Dependency Assessment

### 1. `shop` Module (Most Critical)

| Dependency | Current Version | Latest Stable | Severity | Notes |
|-----------|----------------|---------------|----------|-------|
| Spring Boot | 1.5.19.RELEASE | 3.2.x+ | **High** | EOL since August 2019, no security patches |
| Spring Cloud | Dalston.SR5 | 2023.0.x+ | **High** | EOL, requires major migration |
| spring-cloud-starter-hystrix | (Dalston managed) | N/A (removed) | **High** | Hystrix deprecated, replaced by Resilience4j |
| spring-cloud-starter-eureka | (Dalston managed) | N/A (renamed) | **High** | Renamed to spring-cloud-starter-netflix-eureka-client |
| Log4j (log4j-api, log4j-core) | 2.6.1 | 2.23.x+ | **Medium** | Vulnerable to Log4Shell CVE-2021-44228 and other critical CVEs |
| AspectJ Weaver | 1.9.19 | 1.9.21+ | **Medium** | Moderately outdated |
| OpenTelemetry annotations | 1.19.2-alpha | 2.x+ (stable) | **Medium** | Pre-release alpha version |
| NekoHTML | 1.9.22 | 1.9.22 | Low | Final release, project archived |
| Java target | 1.8 | 21+ | **High** | Approaching/at end of public updates |
| maven-jar-plugin | 3.0.0 | 3.3.0+ | Low | Moderately outdated |

### 2. `stock` Module

| Dependency | Current Version | Latest Stable | Severity | Notes |
|-----------|----------------|---------------|----------|-------|
| Spring Boot | 2.1.3.RELEASE | 3.2.x+ | **High** | EOL since late 2019 |
| Java target | 1.8 | 21+ | **High** | Approaching/at end of public updates |
| Cucumber (info.cukes) | 1.2.5 | N/A | **Medium** | Deprecated group ID, migrated to `io.cucumber` 7.x+ |
| Hamcrest | 2.1 | 2.2+ | Low | Minor version behind |
| versions-maven-plugin | 2.7 | 2.16.x+ | Low | Developer tooling, outdated |
| javax.validation | (via Spring Boot 2.1.x) | jakarta.validation | **Medium** | Uses javax namespace, needs Jakarta migration |

### 3. `instruments` Module

| Dependency | Current Version | Latest Stable | Severity | Notes |
|-----------|----------------|---------------|----------|-------|
| Spring Boot | 2.7.5 | 3.2.x+ | **Medium** | Approaching EOL |
| OpenTelemetry annotations | 1.19.2-alpha | 2.x+ (stable) | **Medium** | Pre-release alpha version |
| Java target | 17 | 21+ | Low | Still supported but not latest LTS |
| javax.persistence | (via Spring Boot 2.7.x) | jakarta.persistence | **Medium** | Uses javax namespace, needs Jakarta migration |

### 4. `products` Module

| Dependency | Current Version | Latest Stable | Severity | Notes |
|-----------|----------------|---------------|----------|-------|
| Spring Boot | 3.2.2 | 3.2.x+ (latest patch) | Low | Recent version, patch updates available |
| OpenTelemetry annotations | 2.2.0 | 2.x+ (stable) | Low | Reasonably current |
| Java target | 17 | 21+ | Low | Still supported |

### 5. `conductors` Module

| Dependency | Current Version | Latest Stable | Severity | Notes |
|-----------|----------------|---------------|----------|-------|
| Spring Boot | 3.2.2 | 3.2.x+ (latest patch) | Low | Recent version |
| Java target | 17 | 21+ | Low | Still supported |

### 6. `annotator` Module

| Dependency | Current Version | Latest Stable | Severity | Notes |
|-----------|----------------|---------------|----------|-------|
| JavaParser | 3.23.1 | 3.25.x+ | Low | Moderately outdated |
| OpenTelemetry annotations | 1.19.1-alpha | 2.x+ (stable) | **Medium** | Pre-release alpha version |
| exec-maven-plugin | 3.1.0 | 3.2.x+ | Low | Moderately outdated |
| Java target | 17 | 21+ | Low | Still supported |

### 7. Root POM

| Dependency | Current Version | Latest Stable | Severity | Notes |
|-----------|----------------|---------------|----------|-------|
| JUnit | 3.8.1 | 5.10.x+ (JUnit 5) | **Medium** | Extremely outdated, 3 major versions behind |

## javax → Jakarta Namespace Migration

The `shop` (Spring Boot 1.5.x), `stock` (Spring Boot 2.1.x), and `instruments` (Spring Boot 2.7.x) modules all use the `javax.*` namespace for persistence, validation, and annotations. Spring Boot 3.x requires the `jakarta.*` namespace. This is a mandatory migration step when upgrading to Spring Boot 3.x.

**Affected packages:**
- `javax.persistence.*` → `jakarta.persistence.*` (instruments, stock)
- `javax.validation.*` → `jakarta.validation.*` (stock)
- `javax.annotation.*` → `jakarta.annotation.*` (instruments, stock)
- `javax.naming.*` → `jakarta.naming.*` (shop — `NoPermissionException`)

## Related Documents

- [Summary](summary.md)
- [Maintenance Burden](maintenance-burden.md)
- [Remediation Plan](remediation-plan.md)
- [Root-level Technical Debt Report](../technical-debt-report.md)

---

[← Back to README](../README.md)
