> ⚠️ **Early Access**: Behavior documentation is in early access. Please review critically.

# Business Logic

## Shop Module — HomeController

### Product Retrieval by Location
- **Source**: `HomeController.getProductsAllLocations()` (line ~33, `shop/src/main/java/.../controllers/HomeController.java`)
- Default user name: "Guest", default location: "California", default userid: "X0000"
- Calls `productService.getProducts(location)` which delegates to `ProductRepo`
- Calls `instrumentService.getInstruments(location)` for instruments
- Tracks latency for Utah and Colorado locations using static fields `s_utahLatency` and `s_coloradoLatency`

### Location-Based Product Routing (ProductRepo)
- **Source**: `ProductRepo.getProductDTOs()` (line ~37, `shop/src/main/java/.../repo/ProductRepo.java`)
- If `bConductorsEnabled` is `true` AND location is "Utah" → calls `conductors` service
- Otherwise → calls `products` service
- `bConductorsEnabled` is a static boolean, always `true`

### Instrument Retrieval with Locale Filtering
- **Source**: `InstrumentService.getInstruments()` (line ~28, `shop/src/main/java/.../services/InstrumentService.java`)
- Fetches instruments by location from InstrumentRepo
- For each InstrumentDTO, attempts `buildForLocale()` which checks `isEnglish(title)` using regex
- If `isEnglish` fails, falls back to `buildIt()` (without title)
- Note: The `isEnglish` check is commented out (throw is disabled), so non-English titles are accepted

### Stock Aggregation
- **Source**: `ProductService.getProducts()` (line ~24, `shop/src/main/java/.../services/ProductService.java`)
- Merges `ProductDTO` data from products service with `StockDTO` data from stock service
- If no stock entry found for a product, uses `DEFAULT_STOCK_DTO` (SKU "default", quantity 999)

### User Permission Checking
- **Source**: `HomeController.allParameters()` / `checkIfRestricted()` (line ~95, `shop/src/main/java/.../controllers/HomeController.java`)
- Calls `checkIfRestricted(userid)` — currently always returns `false` (HTTP call is commented out)
- If restricted, throws `NoPermissionException`

### Exercise Scoring System
- **Source**: `Exercises.checkExercise()` (line ~52, `shop/src/main/java/.../Exercises.java`)
- 15 exercises for APM training, each validated differently
- Exercises check for: access tokens, trace counts, latency patterns, annotation presence, custom function names
- Scores persisted via `PropertiesUpdater` to file-based properties

### Latency Tracking
- **Source**: `HomeController.getProductsAllLocations()` (line ~60)
- For Utah: records max latency in `s_utahLatency`, resets Colorado latency to 0
- For Colorado: records max latency in `s_coloradoLatency`
- Exercise 4 checks if Colorado latency > 1.2× Utah latency (detecting intentional slowdown)

---

## Products Module — ProductFilterService

### Product Filtering Pipeline
- **Source**: `ProductFilterService.filterAllProducts()` (line ~20, `products/src/main/java/.../services/ProductFilterService.java`)
- Calls 50+ `myCoolFunction*()` methods sequentially before returning products
- Most functions contain `Thread.sleep()` with small values (0-8ms)
- **Intentional Bug**: `locationLookup11("Colorado")` triggers `myCoolFunction234234234(999)` which sleeps 966-1166ms (random)
- The magic number `999` is returned by `getMyInt()` and triggers the long sleep

### In-Memory Product Data
- **Source**: `ProductService` constructor (line ~12, `products/src/main/java/.../services/ProductService.java`)
- 5 hardcoded products: Widget ($1.20), Sprocket ($4.10), Anvil ($45.50), Cogs ($1.80), Multitool ($154.10)

---

## Conductors Module — ConductorsController

### Oregon Location Override
- **Source**: `ConductorsController.getProductsByLocation()` (line ~21, `conductors/src/main/java/.../controllers/ConductorsController.java`)
- Incoming `location` parameter is **overridden** to `"Oregon"` on every request
- `FilteredProducts.filterProducts("Oregon")` throws `InvalidLocaleException` (Oregon is disabled)
- Exception is caught by empty catch block — products are returned normally from `ProductFilterService`
- ProductFilterService in conductors has all filtering **commented out** — just returns products directly

---

## Instruments Module — InstrumentService

### Location-Based Instrument Retrieval
- **Source**: `InstrumentService.getInstruments()` (line ~29, `instruments/src/main/java/.../services/InstrumentService.java`)
- Applies locale filter via `FilteredInstrument.filterInstruments(location)`
- If Oregon → throws `InvalidLocaleException` (caught, logs error, returns empty list)
- If Chicago → executes `findInstruments()` (Cartesian product), then falls back to `findAll()`
- All other locations → standard `findAll()` from JPA repository

### Health Check Endpoints
- All services expose a `/healthcheck` endpoint returning HTTP 200
- Docker Compose health checks poll these endpoints every 2 seconds

---

## Stock Module — DataGenerator

### Synthetic Data Generation
- **Source**: `DataGenerator.init()` (line ~26, `stock/src/main/java/.../config/DataGenerator.java`)
- On startup, generates 5 stock records: productIds 1-5, various SKUs, quantities 0-999

---

## Annotator Module — OpenTelemetryAnnotator

### Automatic OTel Annotation
- **Source**: `OpenTelemetryAnnotator.annotateCodebase()` (line ~50, `annotator/src/main/java/.../OpenTelemetryAnnotator.java`)
- Walks directory tree, finds `.java` files
- Parses each file with JavaParser, visits all MethodDeclarations
- Adds `@WithSpan` annotation to methods (excluding main, setters, most getters, health checks)
- Adds `@SpanAttribute` annotation to all method parameters
- Adds OpenTelemetry import statements
- Renames original file to `.javaOLD`, writes annotated version

## Related Documents

- [Workflows](workflows.md) | [Decision Logic](decision-logic.md) | [Error Handling](error-handling.md)
- [Architecture → Components](../architecture/components.md)

---

[← Back to README](../README.md)
