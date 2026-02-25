# Android Live Coding Challenge (Blackjack + AI Allowed)

Base project for Android interviews focused on **Kotlin + Compose + Clean Architecture** with real game business rules.

## Challenge Context

The app implements a basic Blackjack game using a public cards API:

- API: `https://deckofcardsapi.com/`
- Current flow:
  - Initial deal: player gets 2 face-up cards, dealer gets 1 face-up + 1 face-down card.
  - Player actions: `Hit` and `Stand`.
  - Dealer turn after `Stand`: draws cards to try to beat or tie the player while avoiding going over 21.

## Project Architecture

The template is layered to support technical evaluation:

- `domain`
  - `model/Card.kt`
  - `repository/CardGameRepository.kt`
  - `usecase/DrawOpeningHandUseCase.kt`
- `data`
  - `remote/CardGameApi.kt` (Retrofit)
  - `remote/CardGameRemoteDataSource.kt`
  - `remote/dto/*`
  - `repository/CardGameRepositoryImpl.kt`
- `presentation`
  - `cards/CardsViewModel.kt`
  - `cards/CardsUiState.kt`
  - `cards/CardsScreen.kt`
- `di`
  - `ServiceLocator.kt` (simple dependency wiring, no framework)

## Interview Format (15 minutes)

- Total duration: **15 minutes**.
- AI tools integrated in the IDE are explicitly allowed:
  - Cursor, Claude Code, or equivalent tools.
- The interview does not evaluate syntax memorization, but:
  - ability to **break down problems**
  - quality of technical decisions
  - fast validation of outcomes
  - communication of trade-offs

## Responsible AI Usage Guidelines

Candidates are expected to:

- Use AI to speed up mechanical tasks (boilerplate, refactors, scaffolding).
- Always validate generated output (business rules, edge cases, build correctness).
- Explain what they accepted/modified/rejected from AI proposals.
- Keep consistency with the existing architecture.

## Challenges by Level (15 minutes)

> Note: each level is designed to complete **1 main challenge + 1 small optional improvement** within the available time.

### Junior Level

Goal: demonstrate solid UI state handling and basic interaction flow.

Main challenge (pick 1):

- Show an in-memory wins/losses scoreboard during the session.
- Improve status feedback (`Your turn`, `Dealer turn`, `Final result`) with clear UI.
- Add action guards to prevent repeated taps while loading.

Optional improvement:

- Improve UX copy for network error messages.

Evaluation criteria:

- UI and state clarity
- Compose state/recomposition control
- Code readability and structure

### Mid Level

Goal: strengthen business rule modeling and responsibility separation.

Main challenge (pick 1):

- Extract Blackjack score calculation into a reusable and testable unit.
- Implement explicit tie (`push`) and natural blackjack rules in the flow.
- Add in-memory round history (player hand, dealer hand, result).

Optional improvement:

- Add more explicit result messages ("Dealer busts", "Push", etc.).

Evaluation criteria:

- Correct modeling of game rules
- Strong ViewModel + UiState usage
- Ability to isolate business logic

### Senior Level

Goal: design for extensibility, testability, and robustness.

Main challenge (pick 1):

- Design a configurable dealer strategy (for example, soft-17 hit/stand) without breaking the UI.
- Model typed domain errors and map network failures to business-level states.
- Refactor dependency setup to improve testing (clear interfaces + practical fake/stub).

Optional improvement:

- Add minimal unit tests for critical rules (score + dealer decision).

Evaluation criteria:

- Practical SOLID usage (SRP/DIP/OCP)
- Design quality under time pressure
- Technical justification of decisions and trade-offs

## Cross-Cutting Evaluation Criteria

- Incremental delivery of value in a short time frame.
- Functional correctness over number of changes.
- Handling of key errors and edge cases.
- Technical communication quality while coding.
- Effective AI usage as a multiplier, not as a black box.

## Run Instructions

1. Open `android-live-coding-template` in Android Studio.
2. Sync Gradle.
3. Run the `app` module on an emulator/device.

If building from terminal:

```bash
./gradlew :app:assembleDebug
```

## Recommended Post-Interview Evolutions

- Migrate `ServiceLocator` to Hilt/Koin.
- Add a local layer (Room/DataStore) for persistent stats.
- Complete unit test coverage for Blackjack rules.


PRUEBA
