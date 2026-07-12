# AI Knowledge Index

This file is a lightweight routing map for project knowledge. Use it to find relevant context without loading every knowledge file by default.

## Loading Rules

- Do not load every file under `docs/ai` by default.
- When a request mentions a feature, entity, or domain concept, check this index first.
- Load only the documents that match the current request.

## Domains

### User

- File: `docs/ai/domain/user.md`
- Keywords: user, users, current user, paired user, friend, mate, name, pair, pairing
- Read when changing user identity, naming, local user persistence, room membership, pairing, paired-user lookup, or friend display behavior.

### Watchlist

- File: `docs/ai/domain/watchlist.md`
- Keywords: watchlist, watchlists, liked, disliked, matches, watched, saved movies, movie library
- Read when changing watchlist behavior, persistence, UI, naming, default list semantics, or movie decision history.

## Logic

### Update Movie Status

- File: `docs/ai/logic/update_movie_status.md`
- Keywords: update movie status, movie status, liked, disliked, undefined, reaction, match, paired user, swipe, movie details
- Read when changing movie status updates, swipe decisions, movie details status controls, reaction creation, or match activation/deactivation behavior.

## Adding Knowledge

- Add cross-feature business/domain knowledge under `docs/ai/domain`.
- Add cross-feature logic notes under `docs/ai/logic`.
- Add feature-specific requirements, decisions, and plans next to the feature under `feature/<name>/spec`.
- Keep this index short: include the file path, keywords, and when to read the document.
- Each knowledge file should explain business value, key vocabulary, invariants, and boundaries.
