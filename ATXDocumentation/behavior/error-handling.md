> ⚠️ **Early Access**: Behavior documentation is in early access. Please review critically.

# Error Handling

## Custom Exception Classes

### InvalidLocaleException
- **Defined in**: shop, products, conductors, instruments modules (each has its own copy)
- **Extends**: `Exception`
- **Usage**: Thrown when an unsupported locale (Oregon) is detected
- **Constructors**: default, message, cause, message+cause, full (enableSuppression, writableStackTrace)

### InstrumentNotFoundException
- **Defined in**: `instruments/src/main/java/.../exceptions/InstrumentNotFoundException.java`
- **Extends**: `Exception`
- **Usage**: Thrown when an instrument is not found by ID
- **Handler**: `InstrumentResource.handleInstrumentNotFound()` — returns HTTP 404 with empty body

### StockNotFoundException
- **Defined in**: `stock/src/main/java/.../exceptions/StockNotFoundException.java`
- **Extends**: `Exception`
- **Usage**: Thrown when stock is not found by productId
- **Handler**: `StockResource.handleStockNotFound()` — returns HTTP 404 with empty body

### NoPermissionException
- **Defined in**: `javax.naming.NoPermissionException` (Java standard library)
- **Usage**: Thrown by `HomeController.allParameters()` if user is restricted (currently never triggered)

---

## Hystrix Fallback Methods

### StockRepo Fallbacks (shop module)
- **Method**: `stocksNotFound()`
- **Triggered by**: `@HystrixCommand` on `getStockDTOs()` and `getInstrumentStockDTOs()`
- **Behavior**: Logs "stocksNotFound *** FALLBACK ***", returns `Collections.emptyMap()`
- **Impact**: Shop page renders without stock data (quantities show as default 999)

### InstrumentRepo Fallback (shop module)
- **Method**: `instrumentsNotFound()`
- **Triggered by**: `@HystrixCommand` on `getinstrumentDTOs()`
- **Behavior**: Logs "Instruments Empty NOT FOUND *** FALLBACK ***", returns `Collections.emptyMap()`
- **Impact**: Shop page renders without instrument data

---

## Empty Catch Blocks (Anti-Pattern)

### ProductFilterService (products and conductors modules)
- **Count**: 30+ empty catch blocks
- **Pattern**: Every `myCoolFunction*()` method wraps `Thread.sleep()` in `try/catch` with empty catch body
- **Example**: `catch (Exception e){ }` — no logging, no re-throwing
- **Impact**: Any `InterruptedException` or other exceptions are silently swallowed

### ConductorsController
- **Location**: `getProductsByLocation()` — line ~26
- **Pattern**: `FilteredProducts.filterProducts("Oregon")` throws `InvalidLocaleException`, caught by empty catch
- **Impact**: Oregon filter failure is silently ignored, products are served normally

### InstrumentService (instruments module)
- **Location**: `getInstruments()` — line ~35
- **Pattern**: `filterInstruments(location)` exception caught, logs error but continues
- **Behavior**: `s_logger.error("Locale Filter Failed on " + location)` — better than empty catch

### InstrumentService (shop module)
- **Location**: `getInstruments()` — line ~28
- **Pattern**: `buildForLocale()` catches `InvalidLocaleException`, calls `e.printStackTrace()`, falls back to `buildIt()`
- **Behavior**: Prints stack trace to stderr, builds instrument without title

---

## Exception Handler Methods

### InstrumentResource (instruments module)
```java
@ExceptionHandler
@ResponseStatus(HttpStatus.NOT_FOUND)
public void handleInstrumentNotFound(InstrumentNotFoundException snfe) {}
```
- Returns HTTP 404 with empty body when `InstrumentNotFoundException` is thrown

### StockResource (stock module)
```java
@ExceptionHandler
@ResponseStatus(HttpStatus.NOT_FOUND)
public void handleStockNotFound(StockNotFoundException snfe) {}
```
- Returns HTTP 404 with empty body when `StockNotFoundException` is thrown

---

## Error Recovery Patterns

| Component | Error Scenario | Recovery Strategy |
|-----------|---------------|-------------------|
| StockRepo | Stock service down | Hystrix fallback → empty map → default stock values |
| InstrumentRepo | Instruments service down | Hystrix fallback → empty map → no instruments shown |
| InstrumentService | Oregon locale | Catches exception → returns empty list |
| InstrumentService (shop) | Non-English title | Catches exception → builds instrument without title |
| ConductorsController | Oregon locale filter | Empty catch block → serves products normally |
| ProductFilterService | Thread.sleep interrupted | Empty catch block → continues to next function |
| HomeController | Restricted user | Throws NoPermissionException (currently never triggered) |

## Related Documents

- [Business Logic](business-logic.md) | [Workflows](workflows.md) | [Decision Logic](decision-logic.md)
- [Analysis → Security Patterns](../analysis/security-patterns.md)

---

[← Back to README](../README.md)
