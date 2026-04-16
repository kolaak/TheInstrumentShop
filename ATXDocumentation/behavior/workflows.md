> ⚠️ **Early Access**: Behavior documentation is in early access. Please review critically.

# Workflows

## Workflow 1: Product and Instrument Retrieval (Main Application Flow)

**Entry Point**: `HomeController.getProductsAllLocations()` — `GET /`

1. User navigates to shop homepage with optional `name`, `location`, `userid` parameters
2. Default values applied: name="Guest", location="California", userid="X0000"
3. Permission check: `allParameters()` → `checkIfRestricted(userid)` (currently always passes)
4. **Product retrieval**: `productService.getProducts(location)`
   - `ProductRepo.getProductDTOs(location)` routes based on location:
     - If Utah → HTTP GET to `conductors:8050/conductors?location=Utah`
     - Else → HTTP GET to `products:8020/products?location={location}`
   - `StockRepo.getStockDTOs()` → HTTP GET to `stock:8030/legacy` (Hystrix-protected)
   - Products and stock data merged into `List<Product>`
5. Latency tracked for Utah and Colorado locations
6. **Instrument retrieval**: `instrumentService.getInstruments(location)`
   - `InstrumentRepo.getinstrumentsByLocation(location)` → HTTP GET to `instruments:8040/instruments?location={location}`
   - Each InstrumentDTO mapped to Instrument with locale validation
7. Model populated with user, products, instruments → Thymeleaf "index" template rendered

---

## Workflow 2: Product Catalog Processing (Products Service)

**Entry Point**: `ProductController.getProductsByLocation()` — `GET /products`

1. Location parameter received (defaults to "California" if null)
2. `ProductService` instantiated (creates 5 hardcoded products in memory)
3. `ProductFilterService.filterAllProducts(location, service)` invoked
4. Filter executes 50+ `myCoolFunction*()` calls:
   - Most are trivial sleeps (0-8ms)
   - For Colorado: `locationLookup11()` → `myCoolFunction234234234(999)` → sleep 966-1166ms
5. Returns `productService.getAllProducts()` (all 5 products)

---

## Workflow 3: Conductor Product Processing (Conductors Service)

**Entry Point**: `ConductorsController.getProductsByLocation()` — `GET /conductors`

1. Location parameter received but **overridden to "Oregon"**
2. `ConductorsService` instantiated (creates same 5 hardcoded products)
3. `FilteredProducts.filterProducts("Oregon")` called → throws `InvalidLocaleException`
4. Exception caught by empty catch block (swallowed)
5. `ProductFilterService.filterAllProducts("Oregon", service)` invoked
6. All filtering is **commented out** — directly returns `productService.getAllProducts()`

---

## Workflow 4: Instrument Data Retrieval (Instruments Service)

**Entry Point**: `InstrumentResource.getInstruments()` — `GET /instruments`

1. Location parameter received (default "California")
2. `InstrumentService.getInstruments(location)` invoked
3. Locale filter: `FilteredInstrument.filterInstruments(location)`
   - Oregon → `InvalidLocaleException` thrown → caught, returns empty list
4. If location is "Chicago":
   - Executes `findInstruments()` — Cartesian product native SQL query
   - Falls back to `findAll()` from JPA repository
5. All other locations: standard `findAll()` from JPA → returns all instruments from PostgreSQL

---

## Workflow 5: OpenTelemetry Annotation (Annotator Tool)

**Entry Point**: `OpenTelemetryAnnotator.main()` — CLI execution

1. Defines target directory: `../products/src/main/java/com/shabushabu/javashop/products`
2. `annotateCodebase(projectDir)` called
3. `listFiles(URI)` walks directory tree recursively, collects all `.java` files
4. For each Java file:
   - Parsed using JavaParser `StaticJavaParser.parse()`
   - `LexicalPreservingPrinter` setup to maintain formatting
   - `OtelAnnotationVisitor` visits each `MethodDeclaration`:
     - Skips: main methods, setters, most getters, health check methods
     - Adds `@WithSpan` annotation to qualifying methods
     - Adds `@SpanAttribute("paramName")` to all parameters
   - OpenTelemetry import statements added to compilation unit
   - Original file renamed to `.javaOLD`, annotated code written to original filename
5. Updates `.env` file to set `Annotated=true`

---

## Workflow 6: Exercise Scoring (Shop Module)

**Entry Point**: `HomeController.getScores()` — `GET /score`

1. If exercise=0: returns all stored scores from `PropertiesUpdater`
2. If exercise>0: validates specific exercise via `Exercises.checkExercise()`
   - Each exercise has unique validation logic (1-15)
   - Results returned as `{"exerciseN": "true/false"}`

## Related Documents

- [Business Logic](business-logic.md) | [Decision Logic](decision-logic.md) | [Error Handling](error-handling.md)
- [Diagrams → Sequence Diagrams](../diagrams/behavioral/sequence-diagrams.md)

---

[← Back to README](../README.md)
