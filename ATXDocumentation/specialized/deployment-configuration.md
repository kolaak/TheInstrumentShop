# Deployment Configuration

## Docker Compose Services

**Source**: `docker-compose.yml`

| Service | Image | Port | Build Context | Health Check |
|---------|-------|------|--------------|-------------|
| shop | shabushabu/javashop.shop:latest | 8010:8010 | ./shop | `curl http://localhost:8010/healthcheck` |
| products | shabushabu/javashop.products:latest | 8020:8020 | ./products | `curl http://localhost:8020/products/healthcheck` |
| conductors | shabushabu/javashop.conductors:latest | 8050:8050 | ./conductors | `curl http://localhost:8050/conductors/healthcheck` |
| stock | shabushabu/javashop.stock:latest | 8030:8030 | ./stock | `curl http://localhost:8030/healthcheck` |
| instruments | shabushabu/javashop.instruments:latest | 8040:8040 | ./instruments | `curl http://localhost:8040/healthcheck` |
| postgresDB | postgres:13.1-alpine | 5432:5432 | — | `pg_isready` |
| redis | redis | (default) | — | — |
| shoptester | shabushabu/javashop.tester:latest | — | ./test | — |

## Health Check Configuration (All Services)

```yaml
healthcheck:
  interval: 2s
  retries: 5
  start_period: 5s
  timeout: 2s
```

## Environment Variables

### Shop Service
- `USERNAME=${SHOP_USER}` — User identification

### Instruments Service
- `USERNAME=${SHOP_USER}`
- `SPRING_DATASOURCE_URL=jdbc:postgresql://postgresDB:5432/instruments`
- `SPRING_DATASOURCE_USERNAME=instruments`
- `SPRING_DATASOURCE_PASSWORD=instruments`
- `SPRING_JPA_HIBERNATE_DDL_AUTO=update`

### PostgreSQL
- `POSTGRES_USER=instruments`
- `POSTGRES_PASSWORD=instruments`

## Volume Mounts

| Service | Source | Target | Purpose |
|---------|--------|--------|---------|
| shop | ./shop/data | /container/shop/data | Properties files for exercises |
| shoptester | ./test/data | /container/test/data | Test data |
| postgresDB | ./db/sql/instruments-latest.sql | /docker-entrypoint-initdb.d/ | Initial data load |
| postgresDB | ./db/sql/instruments-chicago.sql | /docker-entrypoint-initdb.d/ | Chicago data load |

## Service Dependencies

| Service | Depends On | Condition |
|---------|-----------|-----------|
| shop | products, stock, instruments | links |
| instruments | postgresDB | service_healthy |
| shoptester | shop | links |

## Network

- **Network name**: `instrument_shop`
- **Type**: External (must be created before `docker-compose up`)
- All services share this network for DNS-based service discovery

## Related Documents

- [Architecture → System Overview](../architecture/system-overview.md)
- [Diagrams → System Context](../diagrams/architecture/system-context.md)

---

[← Back to README](../README.md)
