# bot-api-spring-boot-starter

## Mission & Overview
A platform-agnostic Spring Boot Starter that defines the core abstractions, context pipeline, event-driven listener model, keyboard system, and persistence contracts for building multi-platform bots (Telegram, Discord, Instagram, Facebook).

### Problems Solved
- **Platform Decoupling**: Abstracts bot lifecycle (`SpringBot`), payload ingestion (`PayloadConsumer`), and outbound API calls (`ClientService`, `Replier`) away from vendor SDKs.
- **Extensible Context Pipeline**: Extracts strongly-typed `Context` records from raw platform payloads into a unified, serializable `ContextRegistry` via ordered `ContextExtractor` components.
- **Transactional Event-Driven Routing**: Wraps payload consumption in a `TransactionTemplate` and publishes `ContextRegistry` events for Spring / Spring Modulith listeners.
- **Declarative UI & Localization**: Provides coordinate-based `KeyboardButton` ordering, listener-scoped button visibility, compact `CallbackData` encoding (`listener:entityId:value`), and locale-aware i18n (`BotMessageSourceService`).

---

## Tech Stack
- **Java**: 25 (Records, Pattern Matching, Sequenced Collections)
- **Framework**: Spring Boot 4.1.1 (Spring Framework 7+)
- **Build System**: Apache Maven (module under `parent-spring-boot-starter:1.0.0`)
- **Key Dependencies**:
  - `com.pavelshapel:ordered-spring-boot-starter` (ordered extractor, replier, and button factory pipelines)
  - `com.pavelshapel:i18n-spring-boot-starter` (`MessageSourceService` base for user-locale resolution)
  - `com.pavelshapel:log-spring-boot-starter` (`LoggerProvider` structured execution logging)
  - `org.springframework:spring-tx` (`TransactionTemplate` for transactional event publishing)
  - `com.fasterxml.jackson.core:jackson-annotations` (polymorphic `@JsonTypeInfo` serialization for contexts and buttons)

---

## Architecture & Core Abstractions

### 1. Ingestion & Context Pipeline
- **`SpringBot<PAYLOAD, PAYLOAD_CONSUMER>`**: Base bot holder binding `BotProperties` (`name`, `token`, `client`, `localizedCommands`) and payload consumer.
- **`PayloadConsumer<PAYLOAD, EXTRACTORS_PROCESSOR>`**: Consumes raw payloads inside `TransactionTemplate` and publishes the assembled `ContextRegistry` via `ApplicationEventPublisher`.
- **`Context` & `ContextRegistry`**: Polymorphic context container keyed by class simple name. Built-in contexts: `BotContext`, `SocialContext`, `ChatContext`, `UserContext`, `MessageContext`, `ListenerContext`, `KeyboardButtonContext`.
- **`ContextExtractor<PAYLOAD>` & `ContextExtractorsProcessor`**: Sequential ordered pipeline converting raw payloads into `Context` instances.

### 2. Event Listeners & Outbound Repliers
- **`Listener<REPLIERS_PROCESSOR>` & `CommandListener`**: Base event handlers implementing `Consumer<ContextRegistry>` with SpEL routing support (`DOT_IS_APPLICABLE_SIGNATURE`), i18n message resolution, event re-publishing, and reply/delete delegation.
- **`Replier<PAYLOAD, CLIENT>` & `RepliersProcessor`**: Ordered strategy components matching payload types to outbound actions (`reply`, `delete`) via `ClientService`.
- **`ClientService<REQUEST, KEYBOARD, KEYBOARD_FACTORY>`**: Abstract outbound client handling `sendMessage`, `editMessage`, `deleteMessage`, keyboard assembly, and logged request execution.
- **`CommandRegistrar<REQUEST, CLIENT>`**: Registers localized bot commands defined in `BotProperties` against the platform API.

### 3. Keyboard &Persistence Contracts
- **`KeyboardButton`, `KeyboardButtonFactory`, `KeyboardFactory`**: Grid-ordered (`row`, `col`) button model with listener-filtered visibility (`visibleInListenerSimpleNames`) and standard emoji constants.
- **`CallbackData`**: Immutable record encoding/decoding `listenerSimpleName:entityId:value` strings.
- **`Entity`, `SocialEntity`, `SocialDaoService`, `SocialPublicService`**: Persistence contracts bridging domain entities and `ContextRegistry` via `BiConverter`.

### 4. Auto-Configuration
- **`BotApiStarterAutoConfiguration`**: Registers `BotProperties` and `@ConditionalOnMissingBean BotMessageSourceService` via `AutoConfiguration.imports`.

---

## Development Guidelines & Rules
1. **Target Runtime**: Maintain strict compatibility with Java 25 and Spring Boot 4+.
2. **Proxy Safety**: Listeners and ordered components may be wrapped in Spring AOP/CGLIB proxies (e.g., `@ApplicationModuleListener`); use `AopProxyUtils.ultimateTargetClass` instead of raw `getClass()` when resolving component names.
3. **Context Immutability**: Treat `Context` implementations as immutable records; avoid mutating shared collections inside `ContextRegistry` without defensive copies.
4. **Proactive & Reactive Parity**: `ClientService` and `ContextRegistry` operations must support both reactive replies (with incoming `MessageContext`) and proactive/scheduled notifications (chat-only context).

---

## Roadmap

- [x] **Phase 1: Init (Current Implementation)**
  - Core ingestion pipeline (`SpringBot`, `PayloadConsumer`, `ContextExtractorsProcessor`, `ContextRegistry`).
  - Built-in context records (`BotContext`, `SocialContext`, `ChatContext`, `UserContext`, `MessageContext`, `ListenerContext`, `KeyboardButtonContext`).
  - Abstract `Listener`, `CommandListener`, `Replier`, `ClientService`, and `CommandRegistrar` contracts.
  - Coordinate-sorted `KeyboardButton` pipeline and `CallbackData` (`listener:entityId:value`) codec.
  - `SocialEntity`, `SocialDaoService`, and `SocialPublicService` persistence abstractions.
- [ ] **Phase 2: Context Safety, Proxy Resilience & Event Routing**
  - Replace raw `getClass().getSimpleName()` in `Listener` with proxy-safe `AopProxyUtils.ultimateTargetClass`.
  - Introduce dedicated target listener routing (`targetListeners` in `ListenerContext` / `ContextRegistry`) so chained events do not overwrite `MessageContext.message`.
  - Harden `ContextRegistry`, `BotMessageSourceService`, and `ClientService` with null-safe/Optional accessors to support proactive scheduled messaging (without incoming `MessageContext.socialId`).
- [ ] **Phase 3: Generic Persistence & Conversational State Contracts**
  - Add base `DaoService<ENTITY, CONVERTER>` and `PublicService` abstractions alongside `SocialDaoService` for domain entities (`Entity` implementations like `Event`).
  - Add conversational/dialog state abstractions to route free-text user input during multi-step interactive flows (e.g., entering reminder text and time).
- [ ] **Phase 4: Comprehensive Unit Test Suite**
  - Add isolated unit tests (JUnit 5, Mockito, AssertJ) for `CallbackData`, `ContextRegistry`, `KeyboardFactory`, `Listener`, `CommandListener`, `ClientService`, `CommandRegistrar`, `DaoService`, `SocialDaoService`, and `BotMessageSourceService`.
