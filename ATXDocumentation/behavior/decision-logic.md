> ⚠️ **Early Access**: Behavior documentation is in early access. Please review critically.

# Decision Logic

## Decision Point 1: Product Service Routing (Shop → Products vs. Conductors)

**Location**: `ProductRepo.getProductDTOs()` — `shop/src/main/java/.../repo/ProductRepo.java`, line ~37

```
IF bConductorsEnabled == true AND location equalsIgnoreCase "Utah"
  THEN → HTTP GET conductors:8050/conductors?location=Utah
  ELSE → HTTP GET products:8020/products?location={location}
```

- `bConductorsEnabled` is `static boolean = true` (always enabled)
- Only Utah routes to the conductors service; all other locations go to products

---

## Decision Point 2: Colorado Latency Injection (Products/Conductors)

**Location**: `ProductFilterService.locationLookup11()` — `products/src/main/java/.../services/ProductFilterService.java`, line ~441

```
IF location equalsIgnoreCase "Colorado"
  THEN → call myCoolFunction234234234(getMyInt(location))
    IF 999 == myInt
      THEN → Thread.sleep(random(200) + 966)   // 966-1166ms delay
```

- `getMyInt()` always returns `999`, so the delay always triggers for Colorado
- This is an intentional performance bug for APM training exercises

---

## Decision Point 3: Chicago-Specific Query Path (Instruments)

**Location**: `InstrumentService.getInstruments()` — `instruments/src/main/java/.../services/InstrumentService.java`, line ~39

```
IF location equalsIgnoreCase "Chicago"
  THEN → execute findInstruments() (Cartesian product query)
    IF result is null OR not a List
      THEN → return null
      ELSE → return findAll() (standard JPA query)
  ELSE → return findAll() (standard JPA query)
```

- Chicago triggers a deliberately expensive cross-join query before falling back to normal results

---

## Decision Point 4: Oregon Locale Validation (Instruments & Conductors)

**Location**: `FilteredInstrument.filterInstruments()` — `instruments/src/main/java/.../model/FilteredInstrument.java`, line ~18

```
IF locale equalsIgnoreCase "Oregon"
  IF s_Localedisabled == true   // always true
    THEN → throw InvalidLocaleException("Trying to filter to disabled Region: Oregon")
  ELSE
    THEN → return true (allow Oregon data)
```

- Same logic exists in `conductors/model/FilteredProducts.filterProducts()`
- Oregon is always disabled (`s_Localedisabled = true`)

---

## Decision Point 5: Hystrix Fallback Decisions (Shop)

**Location**: `StockRepo.getStockDTOs()`, `InstrumentRepo.getinstrumentDTOs()` — shop module

```
IF HTTP call to downstream service fails (timeout, connection error, etc.)
  THEN → Hystrix triggers fallback method
    StockRepo → stocksNotFound() → returns Collections.emptyMap()
    InstrumentRepo → instrumentsNotFound() → returns Collections.emptyMap()
```

- Fallbacks return empty collections, allowing the shop to render without stock/instrument data

---

## Decision Point 6: Instrument Locale Check (Shop)

**Location**: `Instrument.buildForLocale()` — `shop/src/main/java/.../model/Instrument.java`, line ~69

```
IF NOT isEnglish(title)
  THEN → (throw commented out — no action taken)
ELSE
  THEN → (no action — continue building instrument)
```

- The `isEnglish` regex check exists but the `throw` is commented out
- Non-English titles are silently accepted

---

## Decision Point 7: User Permission Check (Shop)

**Location**: `HomeController.allParameters()` → `checkIfRestricted()` — line ~95

```
IF checkIfRestricted(userid) == true
  THEN → throw NoPermissionException("User does not have permissions")
ELSE
  THEN → continue processing
```

- `checkIfRestricted()` always returns `false` (HTTP call to Lambda is commented out)

---

## Decision Point 8: Default Parameter Assignment (Shop)

**Location**: `HomeController.getProductsAllLocations()` — line ~35

```
IF name == null    → name = "Guest"
IF location == null → location = "California"
IF userid == null   → userid = "X0000"
```

---

## Decision Point 9: Conductors Location Override

**Location**: `ConductorsController.getProductsByLocation()` — line ~21

```
ALWAYS → location = "Oregon"    // incoming parameter is discarded
```

- Regardless of what location is passed, Oregon is always used

---

## Decision Point 10: Stock Default Value (Shop)

**Location**: `ProductService.getProducts()` — line ~30

```
IF stockDTO == null (no matching stock for product)
  THEN → use DEFAULT_STOCK_DTO (sku="default", amountAvailable=999)
```

## Related Documents

- [Business Logic](business-logic.md) | [Workflows](workflows.md) | [Error Handling](error-handling.md)

---

[← Back to README](../README.md)
