# Project Rules

## Architecture
- Keep feature-first packages under `feature/<name>`.
- UI logic stays in ViewModel/UDF, not Composable.

## Kotlin Style
- Prefer immutable `val` over `var`
- Do not set default value for parameters in functions/constructors except `@Composable`

## Naming
- All variables/parameters/fields with "f" letter. For example, `val fName: String`