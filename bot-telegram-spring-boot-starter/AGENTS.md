# bot-telegram-spring-boot-starter

## Mission & Overview
A Spring Boot Starter implementing the `bot-api-spring-boot-starter` contracts for Telegram using `telegrambots-springboot-longpolling-starter` and OkHttp-backed `TelegramClient`.

### Problems Solved
- **Telegram Long-Polling Integration**: Bridges Telegram `Update` streams into the transactional `PayloadConsumer` and `ContextRegistry` event pipeline.
- **Automated Context Extraction**: Extracts `BotContext`, `SocialContext`, `ChatContext`, `UserContext`, `MessageContext`, and `KeyboardButtonContext` from both text messages and callback queries.
- **Smart Reply Strategy**: Automatically routes replies to either delete-and-send (`TelegramTextReplier`) or inline message editing (`TelegramCallbackReplier`).
- **Inline Keyboard & Command Provisioning**: Maps coordinate-ordered `TelegramKeyboardButton` instances into `InlineKeyboardMarkup` rows and registers localized bot commands on startup (`ApplicationReadyEvent`).

---

## Tech Stack
- **Java**: 25 (Records, Streams, Pattern Matching)
- **Framework**: Spring Boot 4.1.1 (Spring Framework 7+)
- **Build System**: Apache Maven (module under `parent-spring-boot-starter:1.0.0`)
- **Key Dependencies**:
  - `com.pavelshapel:bot-api-spring-boot-starter` (core bot abstractions and context pipeline)
  - `org.telegram:telegrambots-springboot-longpolling-starter:10.0.0` (`SpringLongPollingBot` lifecycle)
  - `org.telegram:telegrambots-client:10.0.0` (`OkHttpTelegramClient` execution)
  - `tools.jackson.core:jackson-databind` (Jackson 3 builder deserialization for `TelegramKeyboardButton`)

---

## Architecture & Core Abstractions

### 1. Bot Lifecycle & Payload Ingestion
- **`TelegramSpringBot`**: Package-private `SpringBot<Update, TelegramPayloadConsumer>` implementing `SpringLongPollingBot`.
- **`TelegramPayloadConsumer`**: Consumes `Update` via `LongPollingSingleThreadUpdateConsumer` and delegates to `consumeRawPayload`.
- **`TelegramCommandRegistrar`**: Sends `SetMyCommands` per configured locale (plus default locale) when `bot.localized-commands.commands[0].command` property is present.

### 2. Context Extractors (`TelegramContextExtractor`)
- Base `TelegramContextExtractor` maps Telegram `User` -> `UserContext` and `Chat` -> `ChatContext` (defaulting timezone to `UTC`).
- **Global Extractors**: `TelegramBotContextExtractor`, `TelegramSocialContextExtractor`, `TelegramKeyboardButtonContextExtractor`.
- **Text Extractors** (`update.hasMessage()`): `TelegramTextChatContextExtractor`, `TelegramTextUserContextExtractor`, `TelegramTextMessageContextExtractor`.
- **Callback Extractors** (`update.hasCallbackQuery()`): `TelegramCallbackChatContextExtractor`, `TelegramCallbackUserContextExtractor`, `TelegramCallbackMessageContextExtractor`.

### 3. Outbound Client, Repliers & Keyboards
- **`TelegramClientService`**: Executes `SendMessage`, `EditMessageText`, and `DeleteMessage` via `TelegramClient`, wrapping `TelegramApiException` in `ClientServiceApiException`.
- **`TelegramReplier` & `TelegramRepliersProcessor`**: `TelegramTextReplier` deletes the user command message and sends a new reply; `TelegramCallbackReplier` edits the existing inline message in place.
- **`TelegramKeyboardButton` & `TelegramKeyboardFactory`**: Extends `InlineKeyboardButton` with `row`, `col`, `visibleInListenerSimpleNames`, and button styles (`primary`, `success`, `danger`), grouped into sorted `InlineKeyboardRow` lists.
- **`TelegramListener` & `TelegramCommandListener`**: Base classes pre-bound to `TelegramRepliersProcessor`.

### 4. Auto-Configuration
- **`BotTelegramStarterAutoConfiguration`**, **`TelegramContextExtractorAutoConfiguration`**, **`TelegramReplierAutoConfiguration`**: Registered via `AutoConfiguration.imports`.

---

## Development Guidelines & Rules
1. **Target Runtime**: Strict compatibility with Java 25, Spring Boot 4+, and TelegramBots 10.x.
2. **Update Type Resilience**: Extractors and repliers must safely guard against unsupported or partial `Update` payloads (e.g., `myChatMember`, `InaccessibleMessage`, null `newChatMembers`).
3. **Callback UX**: Handle Telegram callback queries cleanly (e.g., `AnswerCallbackQuery`, avoiding "message is not modified" errors).
4. **Immutability**: Never mutate bound `@ConfigurationProperties` collections in place (e.g., `LocalizedCommands.languageCodes()`).

---

## Roadmap

- [x] **Phase 1: Init (Current Implementation)**
  - Long-polling bot wiring (`TelegramSpringBot`, `TelegramPayloadConsumer`, `OkHttpClient`, `TelegramClientService`).
  - Text and callback `Update` context extractors and `TelegramRepliersProcessor`.
  - Coordinate-sorted `TelegramKeyboardButton` builder and `TelegramKeyboardFactory`.
  - Startup localized command registration via `TelegramCommandRegistrar`.
- [ ] **Phase 2: Payload Resilience & Bug Fixes**
  - Fix `TelegramCommandRegistrar` mutating `localizedCommands.languageCodes()` directly (use a defensive copy).
  - Guard `TelegramKeyboardButtonContextExtractor` and message extractors against non-message/non-callback updates (e.g., group join `myChatMember`), `InaccessibleMessage`, and null `newChatMembers`.
  - Support proactive `sendMessage` in `TelegramClientService` when `MessageContext.socialId()` is absent.
- [ ] **Phase 3: Built-in Shared UI Components & Rich Interactions**
  - Extract reusable `CloseKeyboardButtonFactory` and `CloseKeyboardButtonCallbackListener` into this starter with property-driven auto-configuration.
  - Provide reusable timezone selection listeners (`RegionSettingsListener`, `CitySettingsListener`) or integrate with `bot-persistence-spring-boot-starter`.
  - Add `AnswerCallbackQuery` support and parse mode (MarkdownV2/HTML) configuration in `TelegramClientService`.
- [ ] **Phase 4: Unit & Auto-Configuration Test Suite**
  - Add unit tests for all extractors, repliers, `TelegramKeyboardFactory`, `TelegramClientService`, `TelegramCommandRegistrar`, and auto-configuration wiring.
