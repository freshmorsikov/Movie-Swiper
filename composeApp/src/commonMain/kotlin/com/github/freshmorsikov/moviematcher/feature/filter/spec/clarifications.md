# Filter Feature Requirements Analysis

## Source Feature Request

Reference: proposal.md

## 1. Ambiguities

1. The original proposal says `search field` and `Search genre` but does not define whether search is live, submit-based, case-sensitive, or partial-match.
2. The proposal lists `Click back` but does not define whether leaving the screen saves or discards modified selections.
3. The proposal says `Select genre for filter` but does not define whether selections are applied immediately or staged until confirmation.
4. The proposal identifies a `List of filters` with a selected check, but does not define whether multi-selection is supported implicitly or explicitly.
5. The proposal references selected genres from `GetRoomFlowCaseCase` but does not define the write-side components needed to save updated selections.
6. The proposal does not define whether this feature includes any behavior outside the Filters screen and its own logic.
7. The proposal does not define empty-state behavior when search yields no matching genres.
8. The proposal does not define the semantics of multiple selected genres for downstream filtering logic.

## 2. Missing Information

1. Bottom action requirements were missing: whether the screen needs `Apply`, `Cancel`, or both.
2. Persistence behavior was missing: which write-side components are required after confirmation.
3. Navigation result behavior was missing: whether the screen only closes after save or also returns data directly.
4. Unsaved-change behavior was missing for both back navigation and explicit cancellation.
5. Search result empty-state content was missing.
6. Matching semantics for multiple selected genres were missing.
7. Default state requirements were missing:
   currently selected genres at screen open,
   behavior when no genres are selected.
8. Error/loading requirements were missing for genre source retrieval.
9. Ordering requirements were missing for the genre list.
10. Requirements for search normalization were missing, such as trimming spaces and locale handling.

## 3. Implicit Assumptions

1. The screen supports multi-select because the proposal refers to a list of selected genres.
2. The list of available genres comes from `GetGenreListUseCase` and is expected to be display-ready for the UI.
3. The current applied genre filter already exists in persistent storage and can be loaded when the screen opens.
4. A checked item represents inclusion in the active genre filter.
5. Search is intended to operate on genre names only.
6. The filter screen is expected to be reusable across platforms because it lives under `commonMain`.
7. The feature scope is limited to the Filters screen and its own data flow unless stated otherwise.
8. The app can support bottom action buttons in the existing screen layout.

## 4. Edge Cases

1. The available genre list is empty.
2. Loading genres fails or returns an error.
3. Previously selected genres contain IDs that are no longer present in the available genre list.
4. The user opens the screen, changes selections, and presses system back.
5. The user opens the screen, changes selections, and taps `Cancel`.
6. The user taps `Apply` with no genres selected.
7. The user searches with leading/trailing spaces.
8. The user searches and no results are found.
9. The user has selected genres that are currently hidden by the search query.
10. The genre list is large enough to require stable scroll behavior and efficient rendering.
11. The user taps `Apply` repeatedly.
12. Persistence succeeds but the user leaves the screen before the saved state is re-read.

## 5. Clarifying Questions And Answers

### Question 1

What should happen when the user selects or deselects a genre on the Filters screen?

Possible options offered:
1. Apply immediately: every tap updates the active filter state right away.
2. Stage locally, save on back: changes are kept on the screen and committed only when the user taps back.
3. Stage locally with explicit actions: add `Apply` and `Reset/Clear` controls.

Stakeholder answer:
`add Cancel and Apply buttons on the bottom`

Decision captured:
Selections are staged locally on the screen. The screen must include bottom `Cancel` and `Apply` actions.

### Question 2

What should `Cancel` do if the user changed genre selections but has not tapped `Apply`?

Possible options offered:
1. Discard all unsaved changes and restore the last applied filter state.
2. Keep unsaved changes in memory and only close the screen.
3. Ask for confirmation only if there are unsaved changes, then discard or stay.

Stakeholder answer:
`same as Back button: go back without saving`

Decision captured:
`Cancel` and the back action both close the screen without saving staged changes.

### Question 3

After the user taps `Apply`, where should the selected genres be persisted and what implementation components are required?

Possible options offered:
1. Save to the existing room-backed filter storage and keep scope limited to the Filters screen plus its own retrieval/save logic.
2. Save only for the current app session and clear it after app restart.
3. Return the selected genres to the previous screen only, without storing them centrally.

Stakeholder answer:
`1`

Decision captured:
`Apply` persists the selected genres to existing room-backed filter storage. This specification only covers the Filters screen and the logic required to read and save that state.

### Question 4

How should the search field work on the Filters screen?

Possible options offered:
1. Filter the visible genre list by genre name as the user types, case-insensitive.
2. Filter only after submit/search action.
3. Search should highlight matches but still show the full list.

Please choose one option. Also specify whether partial matches like `act` should match `Action`.

Stakeholder answer:
`1`

Decision captured:
Search filters the visible genre list as the user types and is case-insensitive. The answer strongly implies substring matching; this should be made explicit during implementation acceptance criteria.

### Question 5

What should the screen show when the search returns no matching genres?

Possible options offered:
1. Show an empty state message such as `No genres found`.
2. Show nothing except the empty list.
3. Show a message and an action to clear the search.

Stakeholder answer:
`1`

Decision captured:
The screen must show an empty-state message when search yields no matching genres.

### Question 6

What implementation components are required for this feature package?

Possible options offered:
1. Only define screen behavior and let implementation invent the rest.
2. Explicitly define the feature package components needed for loading, staging, and saving filter selections.
3. Reuse components from another feature package directly without defining local structure.

Stakeholder answer:
`2`

Decision captured:
The specification must explicitly define the feature-local presentation, domain, data, and DI components required to implement the Filters screen.

## 6. Consolidated Functional Requirements

1. The Filters screen shall display:
   a back button in the app bar,
   a search field in the app bar,
   a scrollable list of available genres,
   a selected/unselected state indicator for each genre,
   bottom `Cancel` and `Apply` buttons.
2. The screen shall load the available genres from `GetGenreListUseCase`.
3. The screen shall load the currently applied selected genres from the existing filter storage source referenced as `GetRoomFlowCaseCase`.
4. The user shall be able to select and deselect multiple genres locally without immediately persisting changes.
5. Pressing the back button shall close the screen without saving staged changes.
6. Pressing `Cancel` shall close the screen without saving staged changes.
7. Pressing `Apply` shall persist the staged selected genres to the existing room-backed filter storage.
8. The scope of this feature is limited to the Filters screen and the logic required to load, stage, and save selected genres. Changes to Swipe or other screens are out of scope.
9. Search shall filter the visible genre list as the user types.
10. Search shall be case-insensitive.
11. When the search query yields no matches, the screen shall show a `No genres found` style empty-state message.
12. The feature package shall include explicit components for:
   a presentation-layer contract (`FilterUdf` or equivalent state/action/event model),
   a `FilterViewModel` that manages staged selection state and search query state,
   a domain use case to load available genres,
   a domain use case to observe the currently applied genre filter,
   a domain use case to persist the selected genre filter,
   a data-layer repository or repository methods that read and write the room-backed `genreFilter`,
   a DI module registering all new runtime-owned classes.

## 7. Outstanding Items To Confirm

1. Whether search uses substring matching explicitly, for example `act` matching `Action`.
2. The exact empty-state copy for no search results.
3. Loading behavior while genres or selected filters are being read.
4. What should happen when `Apply` is tapped with zero selected genres:
   remove genre filtering entirely,
   keep previous selection,
   or treat as invalid.
5. Genre ordering rules, for example source order versus alphabetical order.
6. Whether selected genres hidden by the current search query must remain selected and be applied normally.
7. Whether `Apply` should close the screen after successful save.
8. Whether the bottom action buttons should always be enabled or depend on dirty state.

## 8. Implementation Notes For AI Coding Agent

1. Keep UI stateless and render-only in Compose.
2. Handle staged selection state and actions in the presentation layer.
3. Keep persistence and retrieval logic in data/domain layers according to `presentation -> domain -> data`.
4. If new runtime-owned classes are introduced, register them in DI.
5. Reuse existing genre/filter models and flows where possible instead of introducing parallel structures.
6. Do not require any changes in `feature/swipe` or other out-of-scope screens as part of this feature specification.
