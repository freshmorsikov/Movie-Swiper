# Final Filter Feature Requirements

This document supersedes the open questions in proposal.md.

## 1. Scope

The feature covers the Filters screen and the logic required to:
- load available genres,
- load the currently applied room genre filter,
- stage local changes,
- send all selected genres to update the current room in the remote DB,
- close the screen after `Cancel`, back, or successful `Apply`.

Changes to downstream movie filtering on the Swipe screen are not part of this feature implementation, but the remote room filter semantics are defined here so later work uses the same contract.

## 2. Resolved Decisions

1. Selection mode is multi-select.
2. Genre selections are staged locally on the screen and are not persisted until `Apply`.
3. `Back` and `Cancel` do the same thing: close the screen without saving staged changes.
4. `Apply` sends the currently staged genre IDs to update the current room `genreFilter` in the remote DB.
5. After a successful save, the screen closes. No explicit navigation result payload is returned because other screens can observe the updated room flow.
6. Search updates results live while the user types.
7. Search matches genre names only.
8. Search is case-insensitive, trims leading and trailing spaces, and uses substring matching. Example: `act` matches `Action`.
9. Search does not remove selection state. Genres selected before a search stay selected even if the current query hides them.
10. If search returns no matches, the screen shows the message `No genres found`.
11. If no genres are available after loading completes, the screen shows an empty state for the source list. Copy: `No genres available`.
12. Initial selections come from `GetRoomFlowCaseCase`, specifically `room.genreFilter`.
13. If `Apply` is pressed with zero selected genres, the feature sends an empty list to the remote DB update. An empty list means no genre filter is active.
14. Multiple selected genres use inclusive matching semantics for downstream behavior: a movie matches the filter if it has at least one selected genre.
15. Genre ordering is alphabetical by genre name ascending.
16. Stale genre IDs already present in `room.genreFilter` but missing from the available genre list are ignored in the visible checklist. They are removed from persisted state the next time the user taps `Apply`.
17. The screen has loading state for initial data and saving state for `Apply`.
18. This iteration does not define a dedicated error screen. If genre loading produces no data, the screen uses the empty-state behavior above. Retry-specific error UX is out of scope until the data contract exposes a distinct failure state.
19. Bottom action enablement is explicit:
    `Cancel` is always enabled unless a save is currently in progress.
    `Apply` is enabled only when initial data has loaded, no save is in progress, and staged selections differ from the last applied selection.
20. Repeated `Apply` taps are prevented by the saving state. Only one save request may run at a time.

## 3. Functional Requirements

### 3.1 Screen Layout

The Filters screen shall display:
- a top app bar with a back button,
- a search text field,
- a scrollable list of genres,
- a selected or unselected indicator for each visible genre row,
- bottom `Cancel` and `Apply` buttons.

### 3.2 Data Sources

1. Available genres shall be loaded from `GetGenreListUseCase`.
2. Applied filter state shall be loaded from `GetRoomFlowCaseCase`.
3. The updated remote value shall be the current room `genreFilter` field.

### 3.3 Initial State

1. On screen open, the feature shall load both:
   the available genre list,
   the currently applied genre filter.
2. While initial data is loading, the screen shall show a loading state instead of an interactive list.
3. Once loaded, staged selection shall be initialized as the intersection of:
   available genre IDs,
   currently applied `room.genreFilter` IDs.
4. If the applied filter is empty, the screen opens with no selected genres.

### 3.4 Search Behavior

1. Search shall update the visible list on each text change.
2. Search matching shall use the normalized query:
   trim whitespace at both ends,
   compare case-insensitively,
   match if the normalized query is a substring of the genre name.
3. An empty query shall show the full genre list.
4. Search shall not change staged selections.

### 3.5 Selection Behavior

1. Tapping a genre row shall toggle that genre in staged selection.
2. The user may select any number of genres, including zero.
3. The selected indicator shall reflect staged state, not the last remote room state.

### 3.6 Actions

1. Pressing `Back` shall close the screen and discard all staged changes.
2. Pressing `Cancel` shall close the screen and discard all staged changes.
3. Pressing `Apply` shall:
   send only valid currently available selected genre IDs,
   update the current room `genreFilter` in the remote DB,
   prevent duplicate save requests while saving,
   close the screen after a successful save.
4. If save cannot start because initial data is not loaded, `Apply` shall remain disabled.

### 3.7 Empty and Loading States

1. If the loaded genre source list is empty, the screen shall show `No genres available`.
2. If search produces zero matches while genres exist, the screen shall show `No genres found`.
3. The no-results state shall not clear or alter hidden selections.
4. A saving state shall be visible while `Apply` is in progress.

## 4. Data Contract

1. Remote room filter values are genre IDs.
2. `genreFilter = emptyList()` means the room has no active genre filter.
3. Downstream consumers of `genreFilter` shall interpret multiple selected IDs with OR semantics:
   include a movie if it contains at least one selected genre ID.

## 5. Architecture Requirements

The feature package must follow `presentation -> domain -> data`.

### 5.1 Presentation

The feature shall define:
- `FilterUdf` or equivalent state/action/event contract,
- `FilterViewModel`,
- stateless Compose screen content.

The presentation state shall represent at minimum:
- loading state,
- saving state,
- search query,
- full available genre list,
- visible filtered genre list,
- applied selection,
- staged selection,
- whether there are unsaved changes.

The presentation actions shall cover at minimum:
- updating the search query,
- toggling a genre,
- pressing back,
- pressing cancel,
- pressing apply,
- applying loaded data,
- save success.

The presentation events shall cover at minimum:
- close screen after cancel or back,
- close screen after successful apply.

### 5.2 Domain

The feature shall define or reuse explicit use cases for:
- loading available genres,
- observing the current room filter state,
- saving the selected room filter state.

The save use case shall accept the final selected genre ID list and send it to update the current room `genreFilter` in the remote DB.

### 5.3 Data

The feature shall use the existing room owner and remote room record instead of creating parallel filter storage.

Implementation must provide write-side support for updating room `genreFilter`, which currently does not exist. This requires:
- a repository write method for room genre filter updates,
- the corresponding remote API method for updating the room record.

### 5.4 DI

All new runtime-owned classes introduced for this feature shall be registered in a feature DI module.

## 6. Acceptance Criteria

1. Opening the Filters screen with an existing room filter shows those genres preselected.
2. Selecting or deselecting genres does not affect the remote room state until `Apply`.
3. Pressing back after edits discards them.
4. Pressing `Cancel` after edits discards them.
5. Pressing `Apply` sends the staged list to update the current room in the remote DB and closes the screen.
6. Pressing `Apply` with no selected genres clears the remote room filter by sending an empty list.
7. Typing `act` shows `Action` in results.
8. Typing a query with extra spaces behaves the same as the trimmed query.
9. Searching to zero results shows `No genres found`.
10. Selected genres hidden by the current search remain selected and are still saved on `Apply`.
11. Genres are displayed in ascending alphabetical order by name.
12. Stale saved genre IDs that no longer exist in the available list are not shown and are dropped on the next successful save.
