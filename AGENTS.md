# AI agent guidelines

You MUST follow these guidelines every time before answering questions or editing code in this repository.
You MUST prioritize code quality, app stability, maintainability, and a smooth third-party developer experience.
You MUST search existing implementations before creating new code.
You MUST refactor duplicated logic into shared utilities.

## Architecture (strict)
- You SHOULD keep feature-first packages under `feature/<name>`.
- You MUST enforce layering as `presentation -> domain -> data`. You MUST NOT reverse the layering direction.
  - UI (Compose) MUST be stateless and render-focused.
  - Presentation (ViewModel) MUST handle actions and produce new UI state and events.
  - Domain (UseCase) MUST contain business logic and MUST NOT hold state.
  - Data (Repositories) MUST manage data sources and handle data input/output.
- All new classes that own runtime dependencies MUST be registered in DI module.

## Kotlin conventions
- You SHOULD prefer `val` over `var`.
- You MUST NOT use default args in functions/constructors except `@Composable`.
- You SHOULD keep functions small and side effects explicit.
- You MUST add and keep null-safety checks at boundaries.

## Project structure
The Kotlin Multiplatform source sets are split by responsibility: 
- `commonMain` contains shared code.
- `androidMain` contains Android-specific implementations.
- `iosMain` contains iOS-specific implementations.
Shared code MUST live in `commonMain`, while platform-specific details MUST stay inside their respective platform directories.

## Definition of Done
- You MUST ensure that build is successful `./gradlew :composeApp:assembleDebug`
