# Watchlist

Metadata:

- Domain: Watchlist
- Keywords: watchlist, watchlists, liked, disliked, matches, watched, saved movies, movie library, movie status, reactions

## Overview

Watchlists give people a simple way to remember what they felt about movies and what is worth discussing next.
They turn swipe decisions into an organized movie library that remains useful after the first reaction.

The domain has four default watchlists:

- Liked: movies the user is interested in and may want to revisit.
- Disliked: movies the user has ruled out, reducing repeated consideration.
- Matches: movies both paired users are interested in, making it easier to choose something together.
- Watched: movies the user has already seen, helping separate past viewing from future choices.

## Business value

Watchlists reduce decision fatigue.
Instead of forcing users to remember past reactions or repeat the same choices, Watchlists preserve intent and make movie selection faster, clearer, and more collaborative.

Watchlists also create a foundation for future value: personal taste history, shared planning, post-watch reflection, and better recommendations based on what users liked, rejected, matched on, or completed.

## Current behavior

Watchlists are derived views over existing movie and reaction data, not independent user-created collections.

- Liked comes from movies with Liked status.
- Disliked comes from movies with Disliked status.
- Matches comes from the shared match list for the current room.
- Watched is part of the domain vocabulary, but currently has no backing data source and is shown as an empty list.

The Watchlists screen shows all four default watchlists in a stable order: Liked, Disliked, Matches, Watched.
Each watchlist exposes a movie count and up to three preview movies.
