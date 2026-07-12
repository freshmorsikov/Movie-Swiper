# Update Movie Status Logic

Metadata:

- Logic: Update movie status
- Keywords: update movie status, movie status, liked, disliked, undefined, reaction, match, paired user, swipe, movie details

## Overview

Updating a movie status captures a person's decision about a movie and, when the person is paired, may update the shared match state for the pair.

The behavior should be the same whether the decision comes from swiping or from movie details:
the person expresses interest, rejection, or no decision, and the app keeps personal history and shared pair outcomes consistent with that decision.

## Current behavior

Each status update has two product responsibilities:

- Preserve the person's own decision so the movie appears in the right personal context.
- Reconcile the pair's shared outcome when the decision expresses a clear like or dislike.

If the person is not paired, the decision still matters as personal history.
If the person is paired, liked and disliked decisions also contribute to shared matching.
An undefined status means the person has no active like/dislike intent for shared matching.

## Status rules

`Liked`:

- The movie becomes part of the person's liked history.
- The decision represents positive interest in the movie.
- If the paired person's effective reaction also represents positive interest, the movie becomes an active shared match.
- If the paired person has not shown positive interest, the movie remains only this person's liked movie.

`Disliked`:

- The movie becomes part of the person's disliked history.
- The decision represents rejected interest in the movie.
- If the movie was previously a shared match, it should no longer be treated as an active match because mutual interest has been broken.

`Undefined`:

- The movie has no active personal like/dislike decision.
- The status does not express shared matching intent.
- It should not create, activate, or deactivate a shared match.

## Match meaning

A shared match means both paired people have positive intent for the same movie.
The product meaning is mutual interest, not the implementation detail used to evaluate that interest.

When reaction history contains more than one decision for the same person and movie, the product should treat that history as an evolving preference.
A later rejection can withdraw earlier interest, and a later like can restore it.

## Boundaries

This document describes product behavior, not the technical placement of that behavior.
The same product rules remain valid regardless of how the app is built internally.
