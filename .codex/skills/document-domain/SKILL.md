---
name: document-domain
description: Create, review, or update project AI domain knowledge documents for a named domain under docs/ai/domain and keep docs/ai/index.md in sync. Use when the user asks to document a domain, entity, bounded context, or business concept such as User or Watchlist; add metadata, keywords, Overview, Business value, Current behavior, or Boundaries sections; or review an existing domain markdown file against the implementation. The domain name should be supplied by or inferred from the request.
---

# Document Domain

## Input

Treat the domain name mentioned in the user request as `domain_name`.

Examples:

- `Use $document-domain User`
- `Create AI domain md for Room`
- `Review Watchlist domain doc`

If the user does not provide a domain name and it cannot be safely inferred from the request, ask one concise question before editing.

Normalize the domain file name to lowercase hyphen-case:

- `User` -> `docs/ai/domain/user.md`
- `Movie Status` -> `docs/ai/domain/movie-status.md`
- `Watchlist` -> `docs/ai/domain/watchlist.md`

## Workflow

1. Load routing context.
   - Read `AGENTS.md`.
   - Read `docs/ai/index.md` if it exists.
   - If the target domain already appears in the index, read its domain file before editing.

2. Inspect the implementation before writing.
   - Use `rg` to search for the domain name, close synonyms, UI strings, models, use cases, repositories, and navigation routes.
   - Prefer `composeApp/src/commonMain` for shared behavior.
   - Read only the files needed to understand user-facing behavior and domain invariants.

3. Separate product behavior from code capability.
   - Document what the app actually exposes today.
   - Do not claim a workflow exists just because a lower-level repository or use case can support it.
   - If code contains a placeholder, unbacked state, or future-looking concept, say so explicitly.

4. Create or update `docs/ai/domain/<domain>.md`.
   - Keep the doc concise and agent-facing.
   - Explain business meaning, not just class names.
   - Include product-language current behavior verified against the implementation.
   - Include boundaries that should protect future work.

5. Update `docs/ai/index.md`.
   - Add or update one short entry under `## Domains`.
   - Include the file path, keywords, and when to read the file.
   - Keep the index as a routing map, not a second copy of the domain doc.

6. Verify.
   - Read back the changed markdown.
   - Review the relevant diff.
   - Run the repository Definition of Done from `AGENTS.md` when required.

## Domain Doc Shape

Use this shape by default:

```markdown
# <Domain>

Metadata:

- Domain: <Domain>
- Keywords: <comma-separated search terms>

## Overview

<What this domain means in the product.>

## Business value

<Why this domain matters to users and the app.>

## Current behavior

<Product behavior verified against the implementation. Prefer bullets for user-visible rules, invariants, and derived state.>

## Boundaries

<What future changes should not assume or duplicate.>
```

Omit `## Boundaries` only when there is no meaningful boundary to preserve.

## Writing Rules

- Use plain product language.
- Do not include implementation or code terms, class names, function names, package names, file paths, or implementation links in domain docs.
- Prefer stable domain vocabulary over temporary UI labels.
- Avoid speculative future plans unless clearly labeled as future value.
