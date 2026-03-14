# Plan

## Phase 1
Establish the filter feature skeleton and the write-side room filter path so later layers can build on stable package boundaries and persistence contracts.

### Task 1.1
Create the `feature/filter` package structure under `commonMain` with placeholders for `presentation`, `domain`, `data`, and `di`, aligned to the required `presentation -> domain -> data` layering.
Dependencies: None.
Validation criteria: The new package directories and initial source files exist in `commonMain`, and the layout matches the repository's feature-first conventions.

### Task 1.2
Add the room filter write contract in the data layer by identifying the existing room-backed repository path and defining the repository method needed to update `room.genreFilter`.
Dependencies: Task 1.1.
Validation criteria: A repository interface or concrete repository API exists for updating the current room genre filter, and no parallel filter storage abstraction is introduced.

### Task 1.3
Add the corresponding remote data-source or Supabase API update method for persisting the current room `genreFilter` field.
Dependencies: Task 1.2.
Validation criteria: The remote API layer exposes a dedicated update entry point for the room record `genreFilter`, with input shaped as a list of genre IDs.

### Checkpoint 1
Review the created project structure and data write path before domain logic starts.
Dependencies: Tasks 1.1, 1.2, and 1.3.
Validation criteria: A human reviewer confirms the package placement, layering direction, and reuse of the existing room-backed storage flow.

## Phase 2
Define the core filter domain and presentation contract so selection, search, loading, and save semantics are explicit before UI integration.

### Task 2.1
Create or wire the filter-specific domain entry points by reusing `GetGenreListUseCase` and `GetRoomFlowCaseCase` for reads and adding a save use case for room genre filter updates.
Dependencies: Phase 1 complete.
Validation criteria: The feature has explicit domain entry points for load, observe, and save operations, and the save use case accepts the final selected genre ID list.

### Task 2.2
Define `FilterUdf` or equivalent state/action/event contracts covering loading, saving, search query, full and visible genre lists, applied selection, staged selection, unsaved changes, and close-screen events.
Dependencies: Task 2.1.
Validation criteria: The contract file enumerates all required state fields, actions, and events from the specification without embedding persistence logic in the UI layer.

### Task 2.3
Document the normalization and reconciliation rules in code-facing domain logic: alphabetical ordering, trimmed case-insensitive substring search, intersection-based initial selection, and stale ID cleanup on apply.
Dependencies: Tasks 2.1 and 2.2.
Validation criteria: The planned business rules are represented in domain or reducer-facing helpers/use cases with clear ownership outside the Compose UI.

### Checkpoint 2
Review the core domain model and state contract before ViewModel implementation.
Dependencies: Tasks 2.1, 2.2, and 2.3.
Validation criteria: A human reviewer confirms the domain API, state model, and filter semantics match the requirements, especially empty-list saves and stale-ID handling.

## Phase 3
Implement the first major integration by wiring the presentation reducer and stateless screen content together around the approved contracts.

### Task 3.1
Implement `FilterViewModel` to load genres and room state, initialize staged selection from the valid intersection, maintain search results, track unsaved changes, and prevent duplicate apply requests while saving.
Dependencies: Phase 2 complete.
Validation criteria: The ViewModel owns loading and save orchestration, emits close events for back/cancel/success, and no business logic is pushed into the Compose content.

### Task 3.2
Build the stateless Filters screen content with top app bar, search field, genre list rows, loading state, empty states, saving state, and bottom `Cancel` and `Apply` actions driven entirely by presentation state and callbacks.
Dependencies: Task 3.1.
Validation criteria: The screen content renders all required UI states from input state only, with no direct repository or navigation dependencies.

### Task 3.3
Connect the screen wrapper to `FilterViewModel` and event collection so the feature closes on back, cancel, or successful apply without returning a payload.
Dependencies: Tasks 3.1 and 3.2.
Validation criteria: The feature screen observes state and events from the ViewModel, and close behavior is triggered only through presentation events.

### Checkpoint 3
Review the presentation and UI integration before navigation wiring.
Dependencies: Tasks 3.1, 3.2, and 3.3.
Validation criteria: A human reviewer confirms the major component integration preserves stateless UI boundaries and required button-enable behavior.

## Phase 4
Integrate the feature into application wiring and complete automated coverage for the critical behavior set.

### Task 4.1
Register all new runtime-owned filter classes in a dedicated feature DI module and include that module in the central Koin initialization path.
Dependencies: Phase 3 complete.
Validation criteria: The filter DI module exists, all new use cases and the ViewModel are registered, and the app bootstrap includes the feature module.

### Task 4.2
Add the filter navigation route and nav-host entry, and connect the existing swipe filter entry point to open the Filters screen.
Dependencies: Tasks 4.1 and 3.3.
Validation criteria: The navigation model includes a filter destination, the nav host can render it, and the swipe feature has a concrete navigation path into the screen.

### Task 4.3
Add automated tests for reducer or ViewModel behavior covering initial intersection, live search normalization, hidden-selection retention, unsaved-change detection, button enablement, duplicate-save prevention, and stale-ID cleanup on apply.
Dependencies: Tasks 4.1 and 4.2.
Validation criteria: New test files exist under `commonTest`, and the covered scenarios map directly to the acceptance criteria and resolved decisions.

### Checkpoint 4
Review the major app integration and the completed test suite.
Dependencies: Tasks 4.1, 4.2, and 4.3.
Validation criteria: A human reviewer confirms navigation, DI, and test coverage are sufficient before final build verification.

## Phase 5
Perform final integration verification, build validation, and cleanup so the feature is ready for merge.

### Task 5.1
Run the feature through end-to-end acceptance validation using the implemented screen flow, including preselection, local staging, cancel/back discard, empty-list apply, search empty state, and successful close-on-save.
Dependencies: Phase 4 complete.
Validation criteria: Each acceptance criterion from the specification is mapped to a verified scenario, with any gaps explicitly documented.

### Task 5.2
Run `./gradlew :composeApp:assembleDebug` and resolve any compile or wiring issues uncovered by the full app build.
Dependencies: Task 5.1.
Validation criteria: `./gradlew :composeApp:assembleDebug` completes successfully.

### Task 5.3
Remove dead code, temporary scaffolding, and any duplicate logic introduced during implementation, then do a final pass on naming and maintainability.
Dependencies: Task 5.2.
Validation criteria: No unused filter-specific code remains from the implementation, and shared logic has been extracted instead of duplicated.

### Checkpoint 5
Conduct final human review of the integrated feature and merge readiness.
Dependencies: Tasks 5.1, 5.2, and 5.3.
Validation criteria: A human reviewer confirms final integration, build health, and adherence to the specification before approval.
