# Interfaces — REST Endpoints and Public Method Signatures

## REST Endpoints by Service

### Shop Service (port 8010)

| Method | Path | Parameters | Returns | Controller |
|--------|------|-----------|---------|------------|
| GET | `/` | `name` (opt), `location` (opt), `userid` (opt) | Thymeleaf "index" view | `HomeController` |
| GET | `/score` | `exercise` (opt), `data` (opt) | `HashMap<String, String>` | `HomeController` |
| GET | `/healthcheck` | — | `String` (HTTP 200) | `HomeController` |
| GET | `/products` | `location` (opt) | `List<Product>` | `ProductResource` |

### Products Service (port 8020)

| Method | Path | Parameters | Returns | Controller |
|--------|------|-----------|---------|------------|
| GET | `/products` | `location` (required) | `List<Product>` | `ProductController` |
| GET | `/products/healthcheck` | — | `String` (HTTP 200) | `ProductController` |

### Conductors Service (port 8050)

| Method | Path | Parameters | Returns | Controller |
|--------|------|-----------|---------|------------|
| GET | `/conductors` | `location` (required) | `List<Product>` | `ConductorsController` |
| GET | `/conductors/healthcheck` | — | `String` (HTTP 200) | `ConductorsController` |

### Stock Service (port 8030)

| Method | Path | Parameters | Returns | Controller |
|--------|------|-----------|---------|------------|
| GET | `/legacy` | — | `List<Stock>` | `StockResource` |
| GET | `/insruments` | — | `List<Stock>` | `StockResource` (Note: typo in endpoint) |
| GET | `/healthcheck` | — | `String` (HTTP 200) | `StockResource` |

### Instruments Service (port 8040)

| Method | Path | Parameters | Returns | Controller |
|--------|------|-----------|---------|------------|
| GET | `/instruments` | `location` (required, default "California") | `List<Instrument>` | `InstrumentResource` |
| GET | `/stocks` | — | `List<Stock>` | `InstrumentResource` |
| GET | `/healthcheck` | — | `String` (HTTP 200) | `InstrumentResource` |

## Key Public Method Signatures

### Shop Module

```java
// HomeController
public String getProductsAllLocations(Model model, String theName, String theLocation, String userid) throws Exception
public HashMap<String, String> getScores(String exercise, String data)
public void allParameters(String name, String location, String userid) throws NoPermissionException
public boolean checkIfRestricted(String userId)
public String healthCheck()

// ProductService
public List<Product> getProducts(String location)
public List<Product> productsNotFound()

// InstrumentService
public List<Instrument> getInstruments(String location)

// Repos
public Map<String, ProductDTO> getProductDTOs(String location)                 // ProductRepo
public Map<String, StockDTO> getStockDTOs()                                    // StockRepo (@HystrixCommand)
public Map<String, StockDTO> getInstrumentStockDTOs()                          // StockRepo (@HystrixCommand)
public Map<Long, InstrumentDTO> getinstrumentDTOs()                            // InstrumentRepo (@HystrixCommand)
public Map<Long, InstrumentDTO> getinstrumentsByLocation(String location)      // InstrumentRepo
```

### Instruments Module

```java
// InstrumentService
public List<Instrument> getInstruments(String location)

// InstrumentStocksService
public List<Stock> getInstrumentStocks()

// FindInstrumentRepository (interface)
Object findInstruments()
Instrument findInstrumentByID(String id)
```

### Stock Module

```java
// StockService
public List<Stock> getStocks()

// InstrumentStocksService
public List<Stock> getStocks()
public Stock getStock(String productId) throws StockNotFoundException
```

### Annotator Module

```java
// OpenTelemetryAnnotator
public static ArrayList<Path> listFiles(URI path) throws IOException
public static void annotateCodebase(File projectDir) throws Exception
public static void annotateFile(String sFileName) throws Exception
```

## Related Documents

- [API Reference](api-reference.md) | [Data Models](data-models.md) | [Program Structure](program-structure.md)

---

[← Back to README](../README.md)
