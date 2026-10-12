# timezone-spring-boot-starter

## Mission & Overview
A lightweight Spring Boot Starter that provides structured timezone cataloging, region/city parsing, and in-memory search over standard JDK available timezones.

### Problems Solved
- **Structured Timezone Model**: Decomposes IANA timezone identifiers (e.g. `Europe/Moscow`) into strongly-typed `Timezone` records with distinct `region` and `city` parts.
- **Categorization & Filtering**: Pre-filters technical prefixes (`Etc`, `SystemV`) and unsegmented IDs, grouping timezones into sorted navigable structures.
- **Lookup & Search**: Exposes case-insensitive zone search (`searchZoneIds`) and region-specific lookups (`getRegions`, `getZoneIdsByRegion`).
- **Zero-Config Integration**: Auto-configures a singleton `TimezoneRegistry` bean out-of-the-box with conditional fallback support.

---

## Tech Stack
- **Java**: 25 (Records, Streams, Sequenced/Navigable Collections)
- **Framework**: Spring Boot 4.1.1 (Spring Framework 7+)
- **Build System**: Apache Maven (module under `parent-spring-boot-starter:1.0.0`)
- **Key Dependencies**:
  - `org.springframework.boot:spring-boot-starter`
  - `org.springframework.boot:spring-boot-starter-test` (JUnit 5 Jupiter, AssertJ)
  - `org.springframework.boot:spring-boot-configuration-processor`
  - `org.jspecify:jspecify` (nullability annotations)

---

## Architecture & Core Abstractions

### 1. `Timezone`
- Immutable record (`id`, `region`, `city`) implementing `Comparable<Timezone>`.
- Validates non-blank `id` in compact constructor.
- Splits `id` by `/` into `region` and `city` (handles single-segment IDs gracefully).
- Natural ordering sorts by `region` then `city`.

### 2. `TimezoneRegistry`
- In-memory registry aggregating available `ZoneId` entries into `Map<String, Set<Timezone>>`.
- Filters out IDs lacking `/` and technical zones (`Etc/*`, `SystemV/*`).
- Public API returns defensive copies as `TreeSet`:
  - `getRegions()`: Returns sorted set of all unique region names.
  - `getZoneIdsByRegion(region)`: Returns sorted timezones for a given region (empty if missing).
  - `searchZoneIds(query)`: Case-insensitive substring search returning matching zone IDs.

### 3. Configuration & Wiring
- **`TimezoneStarterAutoConfiguration`**: Package-private `@AutoConfiguration` declaring `@ConditionalOnMissingBean TimezoneRegistry`.
- Registered via `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.

---

## Development Guidelines & Rules
1. **Target Runtime**: Strict compatibility with Java 25 and Spring Boot 4+.
2. **Immutability & Safety**: Timezone models are records; registry internal maps and sets must remain immutable.
3. **Defensive Returns**: Return defensive copies (`TreeSet`) from registry lookup methods.
4. **Resilient Lookups**: Search and lookup methods must handle null/blank arguments gracefully without throwing exceptions.
5. **Zero External Footprint**: Rely strictly on JDK standard library (`java.time`) without adding external date/time libraries.

---

## Roadmap

- [x] **Phase 1: Init (Current Implementation)**
  - `Timezone` record with region/city parsing, validation, and natural sorting.
  - `TimezoneRegistry` with region grouping, technical prefix filtering, and case-insensitive search.
  - `TimezoneStarterAutoConfiguration` with `@ConditionalOnMissingBean` registration.
  - Unit and auto-configuration test coverage with JUnit 5 and AssertJ.
- [ ] **Phase 2: Temporal Metadata & Localized Formats**
  - Expose current UTC offset, DST indicators, and localized display names (`ZoneId.getDisplayName`).
- [ ] **Phase 3: Web & Parameter Binding**
  - Add Spring `Converter<String, Timezone>` / `Formatter` for seamless MVC and WebFlux request binding.
