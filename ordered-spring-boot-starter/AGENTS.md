# ordered-spring-boot-starter

## Mission & Overview
A lightweight Spring Boot Starter that provides generic abstractions for building ordered processing pipelines (Chain of Responsibility / Strategy pattern).

### Problems Solved
- **Deterministic Component Ordering**: Discovers and orders Spring beans according to `Ordered` precedence or custom class-based execution sequence.
- **Conditional Execution**: Filters steps via `isApplicable(payload)` so only relevant components run.
- **Dual Processing Strategies**: Supports both sequential synchronous processing and high-throughput parallel execution using Java Virtual Threads.
- **Result Provenance**: Pairs every processing result with the originating component's identifier (`OrderedResult`).
- **Proxy-Resilient Identification**: Resolves component IDs through Spring AOP proxies (`AopProxyUtils.ultimateTargetClass`).

---

## Tech Stack
- **Java**: 25 (Virtual Threads, Records, Pattern Matching, Stream API enhancements)
- **Framework**: Spring Boot 4.0.6 (Spring Framework 7+)
- **Build System**: Apache Maven (module under `parent-spring-boot-starter:1.0.0`)
- **Key Dependencies**:
  - `common-api-spring-boot-starter` (provides `StreamCollectors.toSingle()`)
  - `org.springframework.boot:spring-boot-starter`
  - `org.springframework.boot:spring-boot-starter-test` (JUnit 5 Jupiter, AssertJ)
  - `org.springframework.boot:spring-boot-configuration-processor`

---

## Architecture & Core Abstractions

### 1. `OrderedComponent<PAYLOAD, RESULT>`
- Base abstract class implementing `Function<PAYLOAD, OrderedResult<RESULT>>` and `Ordered`.
- Template method `apply(PAYLOAD)` calls `processPayload(payload)` and wraps the output in `OrderedResult<>(getId(), result)`.
- `getId()` extracts simple class name from the ultimate target class (bypassing Spring/CGLIB proxies).
- `isApplicable(PAYLOAD)` hook (defaults to `true`) for conditional activation.
- Default ordering is `LOWEST_PRECEDENCE`.

### 2. `OrderedComponentsProcessor<PAYLOAD, RESULT, COMPONENT>`
- Package-private abstract base processor managing `List<COMPONENT>`.
- `@PostConstruct init()` establishes deterministic ordering via `getClassesInProcessingOrder()` and caches components by ID in `Map<String, COMPONENT>`.
- Provides lookup methods:
  - `getSingle(payload[, predicate])`: resolves exactly one applicable component using `toSingle()` (throws if 0 or >1).
  - `getSingleById(id)`: retrieves component by identifier.
  - `getApplicableComponentsStream(payload)`: filters components where `isApplicable(payload) == true`.

### 3. Execution Strategies
- **`SequentialOrderedComponentsProcessor`**: In-order sequential execution via Stream API.
- **`ParallelOrderedComponentsProcessor`**: Concurrent execution using `Executors.newVirtualThreadPerTaskExecutor()`, mapping each step to a `CompletableFuture` joined at completion.

### 4. Auto-Configuration
- **`OrderedStarterAutoConfiguration`**: Registered in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.

## Development Guidelines & Rules
1. **Target Runtime**: Always write code compatible with Java 25 and Spring Boot 4+.
2. **Proxy Safety**: Always preserve `AopProxyUtils.ultimateTargetClass(...)` when inspecting classes or generating IDs.
3. **Threading**: Favor Virtual Threads for parallel I/O-bound pipeline tasks; do not introduce heavy fixed thread pools without explicit need.
4. **Separation of Concerns**: Keep business logic in `processPayload`; keep orchestration in processors.

---

## Roadmap

- [x] **Phase 1: Init (Current Implementation)**
  - Split processing into dedicated `SequentialOrderedComponentsProcessor` and `ParallelOrderedComponentsProcessor`.
  - Introduce `OrderedResult<RESULT>` record for component-result binding.
  - Adopt Java Virtual Threads for parallel processing.
  - Integrate proxy-safe target class extraction via `AopProxyUtils`.
  - Integrate `StreamCollectors.toSingle()` for single-component resolution.
- [ ] **Phase 2: Test Suite Modernization**
  - Revamp `OrderedComponentsProcessorTest` (currently commented out) to validate `OrderedResult`, `SequentialOrderedComponentsProcessor`, and `ParallelOrderedComponentsProcessor`.
- [ ] **Phase 3: API Surface & Visibility Review**
  - Verify whether `apply(PAYLOAD)` in processors should remain `protected final` or be exposed as `public` for direct external callers.
- [ ] **Phase 4: Concurrency & Fault Tolerance Enhancements**
  - Optional timeout configuration and error handling strategies (fail-fast vs. best-effort result aggregation) for parallel execution.
