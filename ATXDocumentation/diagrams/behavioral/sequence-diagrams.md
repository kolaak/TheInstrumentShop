# Sequence Diagrams

## Product Retrieval Flow (Non-Utah Location)

```mermaid
sequenceDiagram
    participant U as User Browser
    participant S as Shop :8010
    participant P as Products :8020
    participant ST as Stock :8030

    U->>S: GET /?location=California&name=Guest
    S->>S: allParameters() — permission check
    S->>P: GET /products?location=California
    P->>P: ProductFilterService.filterAllProducts()
    P->>P: myCoolFunction1..N() [Thread.sleep]
    P-->>S: List<ProductDTO> [5 products]
    S->>ST: GET /legacy
    ST-->>S: List<StockDTO> [5 stock records]
    S->>S: Merge products + stock → List<Product>
    S->>S: instrumentService.getInstruments()
    Note over S: (see Instrument Retrieval below)
    S-->>U: HTML page (Thymeleaf index)
```

## Product Retrieval Flow (Utah — via Conductors)

```mermaid
sequenceDiagram
    participant U as User Browser
    participant S as Shop :8010
    participant C as Conductors :8050
    participant ST as Stock :8030

    U->>S: GET /?location=Utah
    S->>C: GET /conductors?location=Utah
    C->>C: location = "Oregon" (hardcoded override)
    C->>C: FilteredProducts.filterProducts("Oregon")
    C->>C: throws InvalidLocaleException (caught, swallowed)
    C->>C: ProductFilterService.filterAllProducts() [no filtering]
    C-->>S: List<ProductDTO> [5 products]
    S->>ST: GET /legacy
    ST-->>S: List<StockDTO>
    S->>S: Merge products + stock
    S-->>U: HTML page
```

## Product Retrieval Flow (Colorado — with Latency Bug)

```mermaid
sequenceDiagram
    participant U as User Browser
    participant S as Shop :8010
    participant P as Products :8020

    U->>S: GET /?location=Colorado
    S->>P: GET /products?location=Colorado
    P->>P: filterAllProducts("Colorado")
    P->>P: myCoolFunction11("Colorado")
    P->>P: locationLookup11("Colorado")
    P->>P: myCoolFunction234234234(999)
    P->>P: Thread.sleep(966-1166ms) ⚠️
    P-->>S: List<ProductDTO> (delayed ~1 second)
    S->>S: Track s_coloradoLatency
    S-->>U: HTML page (slow response)
```

## Instrument Retrieval Flow (Standard)

```mermaid
sequenceDiagram
    participant S as Shop :8010
    participant I as Instruments :8040
    participant DB as PostgreSQL

    S->>I: GET /instruments?location=California
    I->>I: FilteredInstrument.filterInstruments("California")
    I->>I: returns true (not Oregon)
    I->>DB: SELECT * FROM instruments_for_sale (JPA findAll)
    DB-->>I: List<Instrument> [131 records]
    I-->>S: List<Instrument>
    S->>S: Map to shop.model.Instrument with locale check
```

## Instrument Retrieval Flow (Chicago — with Cartesian Product)

```mermaid
sequenceDiagram
    participant S as Shop :8010
    participant I as Instruments :8040
    participant DB as PostgreSQL

    S->>I: GET /instruments?location=Chicago
    I->>I: FilteredInstrument.filterInstruments("Chicago") → true
    I->>DB: SELECT * FROM instruments_for_sale, instruments_for_sale_chicago ⚠️
    Note over DB: Cartesian product: 131 × 86 = 11,266 rows
    DB-->>I: Object (result list)
    I->>DB: SELECT * FROM instruments_for_sale (JPA findAll — fallback)
    DB-->>I: List<Instrument> [131 records]
    I-->>S: List<Instrument>
```

## Related Documents

- [Component Diagrams](../structural/component-diagrams.md)
- [System Context](../architecture/system-context.md)
- [Behavior → Workflows](../../behavior/workflows.md)

---

[← Back to README](../../README.md)
