# Data Models

## Domain Models

### Product (shop module — `com.shabushabu.javashop.shop.model.Product`)
| Field | Type | Access | Description |
|-------|------|--------|-------------|
| id | String | private | Product identifier |
| sku | String | private | Stock keeping unit |
| name | String | private | Product name |
| description | String | private | Product description |
| price | BigDecimal | private | Product price |
| amountAvailable | int | private | Stock quantity |

### Product (products module — `com.shabushabu.javashop.products.model.Product`)
| Field | Type | Access | Annotations | Description |
|-------|------|--------|-------------|-------------|
| id | String | private | @JsonProperty | Product identifier |
| name | String | private | @JsonProperty | Product name |
| description | String | private | @JsonProperty | Product description |
| price | BigDecimal | private | @JsonProperty | Product price |

### Product (conductors module — `com.shabushabu.javashop.conductors.model.Product`)
Same schema as products module Product.

### Instrument (shop module — `com.shabushabu.javashop.shop.model.Instrument`)
| Field | Type | Access | Description |
|-------|------|--------|-------------|
| id | long | **public** | Instrument identifier |
| title | String | **public** | Instrument title |
| price | String | **public** | Price as string |
| instrument_type | String | **public** | Type classification |
| condition | String | **public** | New/Used condition |
| seller_type | String | **public** | Seller classification |
| location | String | **public** | Geographic location |

**Note**: Fields are public (anti-pattern — should be private with getters/setters).

### Instrument (instruments module — `com.shabushabu.javashop.instruments.model.Instrument`)
| Field | Type | Access | Annotations | Description |
|-------|------|--------|-------------|-------------|
| id | long | private | @Id, @GeneratedValue | Auto-generated primary key |
| title | String | private | @Column(nullable=false) | Instrument title |
| sub_title | String | private | @Column(nullable=false) | Subtitle |
| price | String | private | @Column(nullable=false) | Price |
| instrument_type | String | private | @Column(nullable=false) | Type classification |
| condition | String | private | @Column(nullable=false) | Condition |
| post_url | String | private | @Column(nullable=false) | Listing URL |
| seller_type | String | private | @Column(nullable=false) | Seller type |
| location | String | private | @Column(nullable=false) | Location |
| published_date | String | private | @Column(nullable=false) | Publication date |

**JPA Mapping**: `@Entity @Table(name = "instruments_for_sale")`

### Stock (stock module — `com.shabushabu.javashop.stock.model.Stock`)
| Field | Type | Access | Annotations | Description |
|-------|------|--------|-------------|-------------|
| productId | String | private | @Id | Product identifier (PK) |
| sku | String | private | — | Stock keeping unit |
| amountAvailable | int | private | — | Available quantity |

**JPA Mapping**: `@Entity`

### Stock (instruments module — `com.shabushabu.javashop.instruments.model.Stock`)
| Field | Type | Access | Annotations | Description |
|-------|------|--------|-------------|-------------|
| m_id | String | private | @Id, @Column(name="ID") | Stock identifier |
| quantity | String | private | @Column(name="Quantity") | Available quantity as string |

**JPA Mapping**: `@Entity @Table(name = "InstrumentStocks")`

## DTO Models (shop module)

### ProductDTO
| Field | Type | Description |
|-------|------|-------------|
| id | String | Product ID |
| name | String | Product name |
| description | String | Description |
| price | BigDecimal | Price |

### InstrumentDTO
| Field | Type | Description |
|-------|------|-------------|
| id | long | Instrument ID |
| title | String | Title |
| sub_title | String | Subtitle |
| price | String | Price |
| instrument_type | String | Type |
| condition | String | Condition |
| post_url | String | Listing URL |
| seller_type | String | Seller type |
| location | String | Location |
| published_date | String | Publication date |

**Default**: `DEFAULT_INSTRUMENT_DTO` with ID 9999 and placeholder values.

### StockDTO
| Field | Type | Description |
|-------|------|-------------|
| productId | String | Product ID |
| sku | String | SKU |
| amountAvailable | int | Available quantity |

**Default**: `DEFAULT_STOCK_DTO` with empty ID, "default" SKU, and 999 quantity.

## Utility Models

### User (shop module)
| Field | Type | Description |
|-------|------|-------------|
| name | String | User's name |
| location | String | User's location |

### FilteredProducts (conductors module)
| Field | Type | Description |
|-------|------|-------------|
| s_Localedisabled | boolean (static final) | Flag to disable Oregon locale (always `true`) |

### FilteredInstrument (instruments module)
Same schema and behavior as `FilteredProducts` — validates Oregon locale.

## Related Documents

- [Database Schemas](../specialized/database-schemas.md)
- [Interfaces](interfaces.md) | [Program Structure](program-structure.md)

---

[← Back to README](../README.md)
