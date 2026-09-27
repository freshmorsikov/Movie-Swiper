# Movie Swiper

Kotlin Multiplatform (Android and iOS) app using Compose Multiplatform. Shared
application code belongs in `composeApp/src/commonMain`; keep platform-specific
implementations in `androidMain` or `iosMain`.

## Before changing code

- Read `docs/ai/index.md` first. Load only the linked domain or logic documents
  relevant to the change.
- Search for and follow an existing implementation before introducing a new
  pattern, abstraction, or dependency.
- Keep features under `feature/<name>`. Reuse shared code from `shared` or
  `core` instead of duplicating it.

## Architecture

- Preserve the dependency direction: `presentation -> domain -> data`.
  Presentation must not access repositories or data sources directly; domain
  code must not depend on presentation or data implementations.
- Keep Compose UI stateless and render-focused. ViewModels handle user actions,
  expose UI state, and emit one-off events.
- In `UdfViewModel`, `reduce` must be synchronous, deterministic state
  reduction with no I/O, navigation, event emission, or coroutine work. Put
  asynchronous work and follow-up actions in `handleEffects`.
- Register every new class with runtime dependencies in the relevant Koin module
  and include that module from `core/di/Koin.kt` when needed.

## Kotlin and data

- Prefer `val`; do not add default arguments to functions or constructors
  (except `@Composable` functions). Check nullable values at boundaries.
- Add shared code to `commonMain` only when it is platform-independent. Use
  expect/actual or platform source sets for platform APIs.
- Make SQLDelight schema/query changes in `composeApp/src/commonMain/sqldelight`;
  do not hand-edit generated database code.
- Version libraries in `gradle/libs.versions.toml`; do not hardcode dependency
  versions in module build files.
- Never commit or log API keys, tokens, `local.properties`, Firebase service
  files, or other credentials. Use the existing `MOVIE_SWIPER_*` Gradle
  properties and BuildConfig wiring for app configuration.

## Tests and verification

- For changed behavior, add or update tests. Keep domain tests as one test class
  per domain class under `composeApp/src/commonTest` and use `kotlin.test`.
- Run `./gradlew :composeApp:assembleDebug` before considering a code change
  complete. Run the relevant tests when they exist.
- Keep each change focused; remove unused code introduced by the change.
