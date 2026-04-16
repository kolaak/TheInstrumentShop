# Test Specifications

## Test Cases for Migration Validation

### Stock Module Tests

| Test ID | Description | Endpoint | Expected Result |
|---------|-------------|----------|----------------|
| ST-01 | Health check responds | GET /healthcheck | HTTP 200, body contains "200" |
| ST-02 | Legacy stocks returns data | GET /legacy | HTTP 200, JSON array with 5 stock records |
| ST-03 | Instrument stocks returns data | GET /insruments | HTTP 200, JSON array |
| ST-04 | Stock record has required fields | GET /legacy | Each record has productId, sku, amountAvailable |
| ST-05 | Synthetic data generated on startup | GET /legacy | Records include productIds 1-5 |

### Instruments Module Tests

| Test ID | Description | Endpoint | Expected Result |
|---------|-------------|----------|----------------|
| IN-01 | Health check responds | GET /healthcheck | HTTP 200 |
| IN-02 | Get instruments default location | GET /instruments?location=California | HTTP 200, JSON array of instruments |
| IN-03 | Get instruments Chicago | GET /instruments?location=Chicago | HTTP 200, JSON array (non-empty) |
| IN-04 | Get instruments Oregon | GET /instruments?location=Oregon | HTTP 200, empty array |
| IN-05 | Get stocks | GET /stocks | HTTP 200, JSON array |
| IN-06 | Instrument has required fields | GET /instruments?location=California | Each has id, title, price, instrument_type, condition |
| IN-07 | PostgreSQL connection works | GET /instruments?location=California | Returns data from instruments_for_sale table |

### Shop Module Tests

| Test ID | Description | Endpoint | Expected Result |
|---------|-------------|----------|----------------|
| SH-01 | Health check responds | GET /healthcheck | HTTP 200 |
| SH-02 | Homepage renders | GET / | HTTP 200, HTML content |
| SH-03 | Homepage with location | GET /?location=California | HTTP 200, HTML with products |
| SH-04 | Utah routes to conductors | GET /?location=Utah | HTTP 200, products returned |
| SH-05 | Products REST endpoint | GET /products | HTTP 200, JSON array |
| SH-06 | Score endpoint | GET /score?exercise=0 | HTTP 200, JSON map |
| SH-07 | Fallback on downstream failure | Stop products service, GET / | Page renders (empty products) |

### Products Module Tests

| Test ID | Description | Endpoint | Expected Result |
|---------|-------------|----------|----------------|
| PR-01 | Health check responds | GET /products/healthcheck | HTTP 200 |
| PR-02 | Get products by location | GET /products?location=California | HTTP 200, 5 products |
| PR-03 | Colorado latency | GET /products?location=Colorado | Response delayed ~1 second |

### Conductors Module Tests

| Test ID | Description | Endpoint | Expected Result |
|---------|-------------|----------|----------------|
| CO-01 | Health check responds | GET /conductors/healthcheck | HTTP 200 |
| CO-02 | Get products | GET /conductors?location=Utah | HTTP 200, JSON array with products |

### Integration Tests

| Test ID | Description | Steps | Expected Result |
|---------|-------------|-------|----------------|
| IT-01 | End-to-end product flow | Start all services → GET /?location=California | Products with stock data displayed |
| IT-02 | End-to-end Utah flow | GET /?location=Utah | Products served via conductors |
| IT-03 | End-to-end instrument flow | GET /?location=California | Instruments from PostgreSQL displayed |
| IT-04 | Docker Compose deployment | `docker-compose up` | All health checks pass within 30 seconds |

## Related Documents

- [Component Order](component-order.md) | [Validation Criteria](validation-criteria.md)

---

[← Back to README](../README.md)
