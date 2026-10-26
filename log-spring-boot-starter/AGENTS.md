# log-spring-boot-starter

## Mission & Overview
An educational/presentation Spring Boot starter that provides **Dependency Injection for SLF4J `Logger`** via Spring's `InjectionPoint` and a functional mixin interface (`LoggerProvider`) for measuring and logging execution duration.

### Problems Solved
- **Constructor-Injected Loggers**: Eliminates repetitive `LoggerFactory.getLogger(CurrentClass.class)` declarations without code generation (Lombok) by resolving the target class from `InjectionPoint`.
- **Zero-Boilerplate Record Integration**: Aligns `LoggerProvider#logger()` with Java `record` component accessors so record-based services inherit logging helpers automatically.
- **Functional Execution Timing**: Wraps `Supplier<R>` and `Runnable` blocks with standardized entry (`[→]`) and completion (`[←]`) duration logs using monotonic time.

---

## Tech Stack
- **Java**: 25 (Records, Functional Interfaces, Static Imports)
- **Framework**: Spring Boot 4.1.1
- **Build System**: Maven (submodule of `com.pavelshapel:parent-spring-boot-starter:1.0.0`)
- **Key Dependencies**:
  - `org.springframework.boot:spring-boot-starter`: Core Spring context, `InjectionPoint`, auto-configuration, and SLF4J (`org.slf4j.Logger`).
  - `org.springframework.boot:spring-boot-starter-test` (test): JUnit 5, Mockito, AssertJ.

---

## Architecture & Core Abstractions

### 1. `LogStarterAutoConfiguration`
- Package-private `@AutoConfiguration` class registered in `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`.
- Defines `@Bean @Scope(SCOPE_PROTOTYPE) Logger logger(InjectionPoint injectionPoint)`.
- Extracts `injectionPoint.getMember().getDeclaringClass()` to instantiate a class-specific `org.slf4j.Logger`, throwing `BeanInitializationException` if the member is `null`.

### 2. `LoggerProvider`
- Public mixin interface declaring `Logger logger()`.
- Provides overloaded `default` methods `executeAndLog(String message, Supplier<R> supplier)` and `executeAndLog(String message, Runnable runnable)`.
- Measures execution duration via `System.nanoTime()` converted with `TimeUnit.NANOSECONDS.toMillis(...)`.

### 3. Execution / Concurrency Model
- Synchronous, caller-thread execution for `Supplier` and `Runnable` lambdas.
- Prototype-scoped `Logger` creation occurs during Spring dependency injection per target bean instance.

### 4. Configuration & Wiring
- Automatically activated on the classpath via Spring Boot's `AutoConfiguration.imports` mechanism; logs activation banner (`log-spring-boot-starter was applied ✅`) on startup.

---

## Development Guidelines & Rules
1. **Presentation Scope**: Intended for educational/showcase purposes rather than high-scale production (where AOP / Micrometer `@Observed` and static loggers are preferred).
2. **Non-Null by Default**: Assume parameters and return types are non-null unless explicitly annotated with `@Nullable` (e.g., `InjectionPoint#getMember()`). Avoid redundant `Objects.requireNonNull` checks.
3. **Monotonic Timing**: Always use `System.nanoTime()` for elapsed duration calculations; never use `System.currentTimeMillis()`.
4. **Unit Testing Standards**: Keep all tests as pure isolated unit tests (`@ExtendWith(MockitoExtension.class)`, `final` package-private test classes, `sut` field, `result` variable, strict AAA, no Spring context booting).

---

## Roadmap

- [x] **Phase 1: Init (Current Implementation)**
  - Prototype `Logger` bean resolution via `InjectionPoint` in `LogStarterAutoConfiguration`.
  - `LoggerProvider` mixin with `logger()` accessor and `executeAndLog` wrappers using `System.nanoTime()`.
  - Pure unit test suite (`LogStarterAutoConfigurationTest`, `LoggerProviderTest`) aligned with service-layer test standards.
  - Project documentation (`README.md`) with usage examples and architectural disclaimers.
- [ ] **Phase 2: Production-Grade Observability Enhancements (Optional)**
  - Add declarative AOP annotation (e.g., `@LogExecution`) to avoid lambda wrapping and preserve caller line numbers.
  - Add `@ConditionalOnMissingBean` and configuration properties (`@ConfigurationProperties`) to customize log levels and message templates.
