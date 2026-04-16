# API Reference

## Complete HTTP API Specification

### Shop Service — `http://shop:8010`

#### `GET /`
Renders the main shop page with products and instruments for a given user/location.
- **Parameters**: `name` (optional, default "Guest"), `location` (optional, default "California"), `userid` (optional, default "X0000")
- **Response**: Thymeleaf HTML page ("index" template)
- **Side effects**: Increments trace counter, tracks latency for Utah/Colorado locations
- **Error**: Throws `NoPermissionException` if user is restricted

#### `GET /score`
Returns exercise scores or validates an exercise answer.
- **Parameters**: `exercise` (optional, default "0"), `data` (optional, default "")
- **Response**: `HashMap<String, String>` as JSON — e.g., `{"exercise1": "true"}`
- **Behavior**: If exercise=0, returns all scores. Otherwise, validates the given exercise.

#### `GET /healthcheck`
- **Response**: `"HTTP Status OK (CODE 200)"` with HTTP 200

#### `GET /products`
REST endpoint returning products for a given location.
- **Parameters**: `location` (optional, default "California")
- **Response**: `List<Product>` as JSON

---

### Products Service — `http://products:8020`

#### `GET /products`
Returns filtered product list for a given location.
- **Parameters**: `location` (required)
- **Response**: `List<Product>` as JSON (5 hardcoded products: Widget, Sprocket, Anvil, Cogs, Multitool)
- **Behavior**: Passes through `ProductFilterService.filterAllProducts()` which adds artificial latency for Colorado

#### `GET /products/healthcheck`
- **Response**: `"HTTP Status OK (CODE 200)"` with HTTP 200

---

### Conductors Service — `http://conductors:8050`

#### `GET /conductors`
Returns product list. **Note**: Location parameter is accepted but overridden to "Oregon".
- **Parameters**: `location` (required)
- **Response**: `List<Product>` as JSON
- **Bug**: `location = "Oregon"` is hardcoded, then `FilteredProducts.filterProducts("Oregon")` throws `InvalidLocaleException` which is caught and swallowed by an empty catch block

#### `GET /conductors/healthcheck`
- **Response**: `"HTTP Status OK (CODE 200)"` with HTTP 200

---

### Stock Service — `http://stock:8030`

#### `GET /legacy`
Returns all stock records (5 synthetic records generated on startup).
- **Response**: `List<Stock>` as JSON

#### `GET /insruments`
Returns instrument-specific stock records. **Note**: Endpoint has a typo ("insruments" instead of "instruments").
- **Response**: `List<Stock>` as JSON

#### `GET /healthcheck`
- **Response**: `"HTTP Status OK (CODE 200)"` with HTTP 200

---

### Instruments Service — `http://instruments:8040`

#### `GET /instruments`
Returns all instruments from PostgreSQL, with location-based behavior.
- **Parameters**: `location` (required, default "California")
- **Response**: `List<Instrument>` as JSON
- **Behavior for Chicago**: Executes Cartesian product query (`SELECT * FROM instruments_for_sale, instruments_for_sale_chicago`) then falls back to `findAll()`
- **Behavior for Oregon**: Returns empty list (locale filter fails)
- **Behavior for other locations**: Returns all instruments via `findAll()`

#### `GET /stocks`
Returns instrument stock records.
- **Response**: `List<Stock>` as JSON

#### `GET /healthcheck`
- **Response**: `"HTTP Status OK (CODE 200)"` with HTTP 200

---

## Inter-Service Call Map

| Caller | Callee | Endpoint | Condition |
|--------|--------|----------|-----------|
| shop.ProductRepo | products | `GET /products?location={loc}` | location ≠ "Utah" |
| shop.ProductRepo | conductors | `GET /conductors?location={loc}` | location = "Utah" |
| shop.StockRepo | stock | `GET /legacy` | Always |
| shop.StockRepo | stock | `GET /instrumemnts` (typo) | getInstrumentStockDTOs() |
| shop.InstrumentRepo | instruments | `GET /instruments?location={loc}` | Always |

## Related Documents

- [Interfaces](interfaces.md) | [Data Models](data-models.md)
- [Architecture → Components](../architecture/components.md)

---

[← Back to README](../README.md)
