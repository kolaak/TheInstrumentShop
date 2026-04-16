# Security Patterns

## Critical Security Findings

### 1. SQL Injection — Severity: Medium

**File**: `instruments/src/main/java/.../repositories/FindInstrumentRepositoryImpl.java`, line ~32

```java
public Instrument findInstrumentByID(String id) {
    Instrument result = (Instrument) entityManager
        .createQuery("FROM instruments i WHERE i.ID = " + id.toString())
        .getSingleResult();
    return result;
}
```

**Risk**: The `id` parameter is concatenated directly into the HQL query without sanitization. An attacker could inject arbitrary HQL/SQL.

**Fix**: Use parameterized queries:
```java
entityManager.createQuery("FROM Instrument i WHERE i.id = :id")
    .setParameter("id", id)
    .getSingleResult();
```

---

### 2. Log4j 2.6.1 Vulnerability (Log4Shell) — Severity: Medium

**File**: `shop/pom.xml`, lines ~31-37

```xml
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.6.1</version>
</dependency>
```

**Risk**: Log4j 2.6.1 is vulnerable to CVE-2021-44228 (Log4Shell), CVE-2021-45046, and CVE-2021-45105. These allow remote code execution through crafted log messages.

**Fix**: Upgrade to Log4j 2.17.1+ or migrate to SLF4J/Logback.

---

### 3. Cartesian Product Query — Severity: Low

**File**: `instruments/src/main/java/.../repositories/FindInstrumentRepositoryImpl.java`, line ~26

```java
entityManager.createNativeQuery("SELECT * FROM instruments_for_sale, instruments_for_sale_chicago").getResultList();
```

**Risk**: Cross-join without WHERE clause produces 131 × 86 = 11,266 rows. Denial of service vector if triggered repeatedly.

**Trigger**: Only executed when location is "Chicago".

---

### 4. Commented-Out External HTTP Call with Hardcoded URL — Severity: Low

**File**: `shop/src/main/java/.../controllers/HomeController.java`, line ~100 (commented out)

```java
// URL url = new URL("https://mofi2flod5cpeismodr7eonuiu0gkoli.lambda-url.us-west-1.on.aws/?userId=" + userId);
```

**Risk**: If uncommented, this would make HTTP calls to a hardcoded external AWS Lambda URL with user IDs. The URL may no longer be controlled by the project maintainers.

---

### 5. Empty Catch Blocks Hiding Errors — Severity: Low

**Files**: `products/...ProductFilterService.java`, `conductors/...ProductFilterService.java`, `conductors/...ConductorsController.java`

**Risk**: Silently swallowing exceptions prevents detection of runtime errors, security incidents, or data corruption. Over 30 instances across the codebase.

---

### 6. Static Mutable State — Severity: Low

**File**: `shop/src/main/java/.../controllers/HomeController.java`, lines ~25-26

```java
public static long s_coloradoLatency;
public static long s_utahLatency;
```

**Risk**: Static mutable fields in a multi-threaded web server can lead to race conditions and data corruption.

---

## Security Patterns in Use

| Pattern | Implementation | Location |
|---------|---------------|----------|
| Circuit Breaker | Hystrix fallbacks prevent cascading failures | shop module repos |
| Health Checks | `/healthcheck` endpoints for container monitoring | All services |
| JPA Repository | Standard Spring Data patterns (excluding custom impl) | instruments, stock |
| Spring Security | Not implemented | — |
| Input Validation | Minimal (no `@Valid`, no request validation) | All services |
| CORS | Not configured | All services |
| Authentication | Not implemented | All services |

## Related Documents

- [Technical Debt Report](../technical-debt-report.md)
- [Code Metrics](code-metrics.md) | [Dependency Analysis](dependency-analysis.md)

---

[← Back to README](../README.md)
