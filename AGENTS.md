# Project Rules

## Architecture
- Keep feature-first packages under `feature/<name>`.
- UI logic stays in ViewModel/UDF, not Composable.

## Kotlin Style
- Prefer immutable `val` over `var`
- Do not set default value for parameters in functions/constructors except `@Composable`