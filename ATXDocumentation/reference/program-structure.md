# Program Structure

## Module Hierarchy

```
javashop (root POM - com.splunk:javashop)
├── shop/       (com.shabushabu.javashop:javashop.shop)
├── stock/      (com.shabushabu.javashop:javashop.stock)
├── products/   (com.shabushabu.javashop:javashop.products)
├── instruments/ (com.shabushabu.javashop:javashop.instruments)
├── conductors/ (com.shabushabu.javashop:javashop.conductors)
├── annotator/  (com.splunk.otel:annotator)
└── test/       (traffic generator)
```

## Class Hierarchy by Module

### shop — `com.shabushabu.javashop.shop`
```
shop/
├── JavaShopApp                          [@SpringBootApplication, @EnableHystrix]
├── Exercises                            [Singleton - exercise scoring]
├── PropertiesUpdater                    [Singleton - score persistence]
├── controllers/
│   ├── HomeController                   [@Controller - main web controller]
│   └── User                             [POJO - name, location]
├── exceptions/
│   └── InvalidLocaleException           [extends Exception]
├── model/
│   ├── Product                          [POJO - id, sku, name, description, price, amountAvailable]
│   └── Instrument                       [POJO - id, title, price, instrument_type, condition, seller_type, location]
├── repo/
│   ├── ProductRepo                      [@Component - HTTP client for products/conductors]
│   ├── StockRepo                        [@Component - HTTP client for stock, @HystrixCommand]
│   └── InstrumentRepo                   [@Component - HTTP client for instruments, @HystrixCommand]
├── resources/
│   └── ProductResource                  [@RestController - /products]
└── services/
    ├── ProductService                   [@Service - product aggregation]
    ├── InstrumentService                [@Service - instrument aggregation]
    └── dto/
        ├── ProductDTO                   [DTO for products service response]
        ├── InstrumentDTO                [DTO for instruments service response]
        └── StockDTO                     [DTO for stock service response]
```

### products — `com.shabushabu.javashop.products`
```
products/
├── ProductServiceApplication            [@SpringBootApplication]
├── controllers/
│   └── ProductController                [@RestController - /products]
├── exceptions/
│   └── InvalidLocaleException           [extends Exception]
├── model/
│   └── Product                          [POJO with @JsonProperty]
└── services/
    ├── ProductService                   [In-memory DAO - 5 hardcoded products]
    └── ProductFilterService             [Complex filtering with Thread.sleep patterns]
```

### conductors — `com.shabushabu.javashop.conductors`
```
conductors/
├── ConductorsServiceApplication         [@SpringBootApplication]
├── controllers/
│   └── ConductorsController             [@RestController - /conductors]
├── exceptions/
│   └── InvalidLocaleException           [extends Exception]
├── model/
│   ├── Product                          [POJO with @JsonProperty]
│   └── FilteredProducts                 [Oregon locale validation]
└── services/
    ├── ConductorsService                [In-memory DAO - 5 hardcoded products]
    └── ProductFilterService             [Mirror of products filter (all filtering commented out)]
```

### instruments — `com.shabushabu.javashop.instruments`
```
instruments/
├── InstrumentsApplication               [@SpringBootApplication]
├── exceptions/
│   ├── InstrumentNotFoundException      [extends Exception]
│   └── InvalidLocaleException           [extends Exception]
├── model/
│   ├── Instrument                       [@Entity - instruments_for_sale table]
│   ├── FilteredInstrument               [Oregon locale validation]
│   └── Stock                            [@Entity - InstrumentStocks table]
├── repositories/
│   ├── InstrumentRepository             [extends JpaRepository, FindInstrumentRepository]
│   ├── FindInstrumentRepository         [interface - custom query methods]
│   ├── FindInstrumentRepositoryImpl     [native SQL queries - SQL injection vulnerability]
│   └── InstrumentStocksRepository       [extends CrudRepository]
├── resources/
│   └── InstrumentResource               [@RestController - /instruments, /stocks, /healthcheck]
└── services/
    ├── InstrumentService                [@Service - location-based instrument retrieval]
    └── InstrumentStocksService          [@Service - instrument stock retrieval]
```

### stock — `com.shabushabu.javashop.stock`
```
stock/
├── StockManagerApplication              [@SpringBootApplication]
├── config/
│   └── DataGenerator                    [@Component - generates 5 stock records on startup]
├── exceptions/
│   └── StockNotFoundException           [extends Exception]
├── model/
│   └── Stock                            [@Entity - productId, sku, amountAvailable]
├── repositories/
│   ├── StockRepository                  [extends CrudRepository]
│   └── InstrumentStocksRepository       [extends CrudRepository]
├── resources/
│   └── StockResource                    [@RestController - /legacy, /insruments, /healthcheck]
└── services/
    ├── StockService                     [@Service - stock retrieval]
    └── InstrumentStocksService          [@Service - instrument stock retrieval]
```

### annotator — `com.splunk.otel.annotator`
```
annotator/
├── OpenTelemetryAnnotator               [Main class - auto-annotates Java files with OTel]
├── DirExplorer                          [Recursive directory walker]
├── DirExplorerInstrumented              [OTel-annotated directory walker]
└── TestGeneratedInstrumentation         [Class listing test tool]
```

## Related Documents

- [Components](../architecture/components.md) | [Interfaces](interfaces.md) | [Data Models](data-models.md)

---

[← Back to README](../README.md)
