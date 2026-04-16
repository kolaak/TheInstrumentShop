# Database Schemas

## PostgreSQL Database: `instruments`

### Table: `instruments_for_sale`

**Source**: `db/sql/instruments-latest.sql`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| ID | VARCHAR(50) | NOT NULL, PRIMARY KEY | Instrument identifier |
| Title | VARCHAR(140) | NOT NULL | Instrument listing title |
| Sub_title | VARCHAR(58) | NOT NULL | Posting metadata (date, location) |
| Price | VARCHAR(12) | NOT NULL | Price as string (e.g., "Rs 39,000") |
| Instrument_Type | VARCHAR(29) | NOT NULL | Category (Keyboard/Piano, String Instrument, etc.) |
| Condition | VARCHAR(4) | NOT NULL | "New" or "Used" |
| Location | VARCHAR(33) | NOT NULL | Seller location (Sri Lankan cities) |
| Post_URL | VARCHAR(114) | NOT NULL | Original listing URL |
| Seller_type | VARCHAR(14) | NOT NULL | "Member" |
| published_date | VARCHAR(14) | NOT NULL | Publication date string |

**Record count**: 131 instruments
**Data source**: Sri Lankan musical instrument marketplace (ikman.lk)

---

### Table: `instruments_for_sale_chicago`

**Source**: `db/sql/instruments-chicago.sql`

| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| ID | VARCHAR(50) | NOT NULL, PRIMARY KEY | Instrument identifier |
| Title | VARCHAR(140) | NOT NULL | Instrument listing title |
| Sub_title | VARCHAR(58) | NOT NULL | Posting metadata |
| Price | VARCHAR(12) | NOT NULL | Price as string |
| Instrument_Type | VARCHAR(29) | NOT NULL | Category |
| Condition | VARCHAR(4) | NOT NULL | Condition |
| Location | VARCHAR(33) | NOT NULL | Seller location |
| Post_URL | VARCHAR(114) | NOT NULL | Listing URL |
| Seller_type | VARCHAR(14) | NOT NULL | Seller type |
| published_date | VARCHAR(14) | NOT NULL | Publication date |

**Record count**: 86 instruments
**Note**: Same schema as `instruments_for_sale`. Both tables are loaded via Docker entrypoint init scripts.

---

### Table: `InstrumentStocks` (JPA-managed)

**Source**: `instruments/src/main/java/.../model/Stock.java`

| Column | Type | JPA Mapping | Description |
|--------|------|-------------|-------------|
| ID | VARCHAR | @Id | Stock identifier |
| Quantity | VARCHAR | @Column | Available quantity as string |

**Note**: This table is managed by JPA/Hibernate but no SQL init script is provided. Schema is auto-updated via `spring.jpa.hibernate.ddl-auto=update`.

---

## H2 In-Memory Database (Stock Service)

### Table: `Stock` (JPA-managed)

**Source**: `stock/src/main/java/.../model/Stock.java` + `DataGenerator.java`

| Column | Type | JPA Mapping | Description |
|--------|------|-------------|-------------|
| productId | String | @Id | Product identifier |
| sku | String | — | Stock keeping unit |
| amountAvailable | int | — | Quantity available |

**Seed data** (from `DataGenerator.init()`):

| productId | sku | amountAvailable |
|-----------|-----|-----------------|
| 1 | 12345678 | 5 |
| 2 | 34567890 | 2 |
| 3 | 54326745 | 999 |
| 4 | 93847614 | 0 |
| 5 | 11856388 | 1 |

## SQL Anti-Patterns

### Cartesian Product Query
```sql
SELECT * FROM instruments_for_sale, instruments_for_sale_chicago
```
- **File**: `FindInstrumentRepositoryImpl.findInstruments()`
- **Result**: 131 × 86 = 11,266 rows (cross join with no WHERE clause)
- **Triggered by**: Chicago location only

## Related Documents

- [Data Models](../reference/data-models.md)
- [Security Patterns](../analysis/security-patterns.md)

---

[← Back to README](../README.md)
