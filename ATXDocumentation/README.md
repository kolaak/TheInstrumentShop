# Java Instrument Shop — Comprehensive Codebase Analysis

## Executive Summary

This documentation provides a complete analysis of the **Java Instrument Shop**, a multi-module Spring Boot microservices application for managing musical instrument inventory. The analysis identified **significant technical debt** including 3 modules on end-of-life Spring Boot versions, a critical Log4j vulnerability, SQL injection, and deprecated libraries. See the [Technical Debt Report](technical-debt-report.md) for the AWS Transformation Recommendation and full findings.

---

## 📋 Table of Contents

### Overview
- [Project Overview](project-overview.md) — Project summary, technology stack, and key findings
- [Technical Debt Report](technical-debt-report.md) — ⭐ **Start here** — AWS Transformation Recommendation + executive summary

### 🏗️ Architecture
- [System Overview](architecture/system-overview.md) — High-level architecture, technology stack, deployment model
- [Components](architecture/components.md) — Service descriptions, responsibilities, and key classes
- [Dependencies](architecture/dependencies.md) — Internal service graph and external library versions
- [Patterns](architecture/patterns.md) — Design patterns, architectural styles, and anti-patterns

### 🔄 Behavior *(Early Access)*
- [Business Logic](behavior/business-logic.md) — Business rules for every major component
- [Workflows](behavior/workflows.md) — Application-level process flows
- [Decision Logic](behavior/decision-logic.md) — All decision points and branching logic
- [Error Handling](behavior/error-handling.md) — Exception patterns and recovery strategies

### ⚠️ Technical Debt
- [Technical Debt Report](technical-debt-report.md) — Root-level summary with AWS recommendation
- [Summary](technical-debt/summary.md) — Categorized overview
- [Outdated Components](technical-debt/outdated-components.md) — Module-by-module dependency analysis
- [Maintenance Burden](technical-debt/maintenance-burden.md) — High-maintenance areas
- [Remediation Plan](technical-debt/remediation-plan.md) — Prioritized action items

### 📚 Reference
- [Program Structure](reference/program-structure.md) — Complete class hierarchy
- [Interfaces](reference/interfaces.md) — REST endpoints and method signatures
- [Data Models](reference/data-models.md) — Entity and DTO definitions
- [API Reference](reference/api-reference.md) — Complete HTTP API specification
- [Modules](reference/modules.md) — Module organization and dependencies

### 📊 Analysis
- [Code Metrics](analysis/code-metrics.md) — Lines of code, complexity measurements
- [Complexity Analysis](analysis/complexity-analysis.md) — Module complexity ranking
- [Dependency Analysis](analysis/dependency-analysis.md) — Internal and external dependency mapping
- [Security Patterns](analysis/security-patterns.md) — Security findings and patterns
- [Tech Debt](analysis/tech-debt.md) — Cross-reference summary

### 📐 Diagrams
- [Component Diagrams](diagrams/structural/component-diagrams.md) — Service relationships and class dependencies
- [Sequence Diagrams](diagrams/behavioral/sequence-diagrams.md) — Key interaction flows
- [System Context](diagrams/architecture/system-context.md) — Deployment architecture

### 🔧 Specialized
- [Database Schemas](specialized/database-schemas.md) — PostgreSQL and H2 table definitions
- [Deployment Configuration](specialized/deployment-configuration.md) — Docker Compose setup

### 🚀 Migration
- [Component Order](migration/component-order.md) — Recommended migration sequence
- [Test Specifications](migration/test-specifications.md) — Test cases for validation
- [Validation Criteria](migration/validation-criteria.md) — Success criteria for migration
