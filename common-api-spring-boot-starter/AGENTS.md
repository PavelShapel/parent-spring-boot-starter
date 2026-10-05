# common-api-spring-boot-starter

## Mission & Overview
A lightweight foundation starter that provides shared functional contracts, bidirectional conversion abstractions, and custom stream collectors across the starter ecosystem.

### Problems Solved
- **Bidirectional Transformation**: Provides `BiConverter<SOURCE, TARGET>` with reversible mappings, default backward fallbacks, and functional forward conversion.
- **Fail-Fast Stream Resolution**: Provides `StreamCollectors.toSingle()` to strictly enforce single-element cardinality in Stream pipelines without silent failures.
- **Minimal Starter Baseline**: Clean auto-configuration entry point for core utility abstractions without external bloat.

---

## Tech Stack
- **Java**: 25 (Sequenced Collections, Records in tests, functional interfaces)
- **Framework**: Spring Boot 4.0.6 (Spring Framework 7+)
- **Build System**: Apache Maven (module under `parent-spring-boot-starter:1.0.0`)
- **Key Dependencies**:
  - `org.springframework.boot:spring-boot-starter`
  - `org.springframework.boot:spring-boot-starter-test` (JUnit 5 Jupiter, AssertJ)
  - `org.springframework.boot:spring-boot-configuration-processor`

---

## Architecture & Core Abstractions

### 1. `BiConverter<SOURCE, TARGET>`
- `@FunctionalInterface` specifying `TARGET convertForward(SOURCE source)`.
- Default `convertBackward(TARGET target)` throws `UnsupportedOperationException("Not implemented yet")` if not overridden.
- `reverse()` returns an inverted `BiConverter<TARGET, SOURCE>` by swapping forward and backward conversions.

### 2. `StreamCollectors`
- Utility interface exposing custom `java.util.stream.Collector` implementations.
- `toSingle()`: Collects to a list and enforces `list.size() == 1`, returning `list.getFirst()`. Throws `IllegalArgumentException` on empty or multi-element streams.

### 3. Configuration & Wiring
- **`CommonApiStarterAutoConfiguration`**: Package-private `@AutoConfiguration` entry point logging starter activation.
- Registered via `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.

---

## Development Guidelines & Rules
1. **Target Runtime**: Strict compatibility with Java 25 and Spring Boot 4+.
2. **Stateless & Pure**: Ensure converters and collectors remain stateless, side-effect free, and thread-safe.
3. **Fail-Fast Cardinality**: Prefer strict validation over silent nulls or arbitrary truncation when resolving elements.
4. **Minimal Footprint**: Keep this starter free of heavy external dependencies to prevent classpath pollution in downstream modules.

---

## Roadmap

- [x] **Phase 1: Init (Current Implementation)**
  - Bidirectional converter interface (`BiConverter`) with invertible execution.
  - Fail-fast stream collector (`StreamCollectors.toSingle()`).
  - Spring Boot 4 auto-configuration registration via `AutoConfiguration.imports`.
  - Comprehensive unit test suite with AssertJ and JUnit 5.
- [ ] **Phase 2: Converter Composition & Extended Collectors**
  - Chained converter composition methods (`andThen`, `compose`).
  - Additional stream collectors (e.g. `toSingleOrEmpty()`, `toUnmodifiableMap()`).
- [ ] **Phase 3: Core Functional Primitives**
  - Common result / either abstractions and functional predicates for pipeline validation.
