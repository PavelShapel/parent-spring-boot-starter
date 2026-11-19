# parent-spring-boot-starter

## Mission & Overview
Multi-module Maven parent and collection of reusable Spring Boot starters (`com.pavelshapel:*`) targeting Java 25 and Spring Boot 4.1.2, with selected modules published to GitHub Packages for downstream services such as `mcp-server-mimo`.

### Problems Solved
- **Shared Common Utilities (`common-api-spring-boot-starter`)**: Provides foundational functional contracts (`BiConverter`, `StreamCollectors`), `RuntimeEnvironment` profile metadata, and baseline auto-configuration.
- **Ordered Component Orchestration (`ordered-spring-boot-starter`)**: Abstracts sequential and Virtual Thread parallel execution over `Ordered`/`OrderedComponent` chains.

---

## Tech Stack
- **Java**: 25 (`maven.compiler.release=25`, Virtual Threads)
- **Framework**: Spring Boot 4.1.2 (`spring-boot-dependencies` BOM)
- **Build System**: Apache Maven (`mvn`), multi-module reactor
- **Distribution**: GitHub Packages (`https://maven.pkg.github.com/PavelShapel/parent-spring-boot-starter`) via [.github/workflows/publish.yml](.github/workflows/publish.yml)

---

## Architecture & Core Abstractions

### Module Status
- **Active & Published to GitHub Packages** (used by `mcp-server-mimo`):
  - `parent-spring-boot-starter`: Root POM (`1.0.1`) managing Spring Boot BOM, compiler settings, and `distributionManagement`.
  - `common-api-spring-boot-starter`: Core utilities (`BiConverter`, `StreamCollectors`, `RuntimeEnvironment`, `CommonApiStarterAutoConfiguration`).
  - `ordered-spring-boot-starter`: Ordered processing framework (`OrderedComponent`, `OrderedComponentsProcessor`, `SequentialOrderedComponentsProcessor`, `ParallelOrderedComponentsProcessor`, `OrderedResult`, `OrderedStarterAutoConfiguration`).
- **In Development** (not published to GitHub Packages yet):
  - `json-spring-boot-starter`, `cache-spring-boot-starter`, `log-spring-boot-starter`, `bot-api-spring-boot-starter`, `bot-telegram-spring-boot-starter`, `i18n-spring-boot-starter`, `timezone-spring-boot-starter`, and commented-out modules in [pom.xml](pom.xml).

### Execution / Concurrency Model
- `ParallelOrderedComponentsProcessor` executes applicable `OrderedComponent`s concurrently using `Executors.newVirtualThreadPerTaskExecutor()`.
- `SequentialOrderedComponentsProcessor` executes applicable components in Spring `@Order` sequence.

### Configuration & Wiring
- Spring Boot auto-configuration registered via `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports` in each starter.

---

## Development Guidelines & Rules
1. **Target Runtime**: Java 25 and Spring Boot 4.1.2; keep starters lightweight with minimal transitive dependencies.
2. **Selective Publishing**: Publish only verified, production-ready modules (`-pl .,common-api-spring-boot-starter,ordered-spring-boot-starter`) in [publish.yml](.github/workflows/publish.yml).
3. **Testing**: Every starter must pass unit tests (`mvn -B test -pl <modules>`) before deployment.

---

## Roadmap

- [x] **Phase 1: Init & `mcp-server-mimo` Starters**
  - Upgraded `spring.boot.version` to `4.1.2` on Java 25.
  - Verified `common-api-spring-boot-starter` (13 unit tests) and `ordered-spring-boot-starter` (43 unit tests).
  - Configured GitHub Packages `distributionManagement` and automated publishing workflow ([publish.yml](.github/workflows/publish.yml)) for `parent-spring-boot-starter`, `common-api-spring-boot-starter`, and `ordered-spring-boot-starter`.
- [x] **Phase 2: `RuntimeEnvironment` Auto-Configuration (`1.0.1`)**
  - Added `RuntimeEnvironment` record and `@ConditionalOnMissingBean` bean in `CommonApiStarterAutoConfiguration` with `ApplicationContextRunner` tests (16 total unit tests in `common-api-spring-boot-starter`).
- [ ] **Phase 3: Stabilize Remaining Starters**
  - Complete ongoing work in `bot-api-spring-boot-starter`, `bot-telegram-spring-boot-starter`, and other in-development modules before adding them to GitHub Packages publishing.
