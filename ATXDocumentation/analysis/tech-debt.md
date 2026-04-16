# Tech Debt Assessment

This document provides a cross-reference summary of all technical debt findings. For detailed analysis, see the dedicated [technical-debt/](../technical-debt/) directory.

## Summary Dashboard

| Category | Items | Highest Severity |
|----------|-------|-----------------|
| EOL Runtimes & Frameworks | 5 | **High** |
| Outdated Dependencies | 6 | **Medium** |
| Security Vulnerabilities | 2 | **Medium** |
| Code Quality Issues | 7 | **Low** |
| **Total** | **20** | — |

## Quick Reference

- **[Technical Debt Report](../technical-debt-report.md)** — Executive summary with AWS Transformation Recommendation
- **[Summary](../technical-debt/summary.md)** — Categorized overview of all findings
- **[Outdated Components](../technical-debt/outdated-components.md)** — Module-by-module dependency analysis
- **[Maintenance Burden](../technical-debt/maintenance-burden.md)** — Areas requiring significant maintenance
- **[Remediation Plan](../technical-debt/remediation-plan.md)** — Prioritized action items

## Top 5 Critical Items

1. **Spring Boot 1.5.19 EOL** (shop) — High — No security patches since 2019
2. **Log4j 2.6.1** (shop) — Medium — Vulnerable to Log4Shell RCE
3. **Spring Boot 2.1.3 EOL** (stock) — High — No security patches since 2019
4. **SQL Injection** (instruments) — Medium — Unsanitized query parameter
5. **Spring Cloud Dalston EOL** (shop) — High — Blocks framework upgrades

## Related Documents

- [Code Metrics](code-metrics.md) | [Dependency Analysis](dependency-analysis.md) | [Security Patterns](security-patterns.md)

---

[← Back to README](../README.md)
