# User

Metadata:

- Domain: User
- Keywords: user, users, current user, paired user, friend, mate, name, pair, pairing

## Overview

A user represents one person using the app.
The user identity connects personal actions, movie reactions, pairing state, and the current room.

The domain uses two important user concepts:

- Current user: the locally remembered user for this app install.
- Paired user: another user in the same room, shown as the friend or mate for shared matching.

## Business value

Users make movie matching personal and collaborative.
The current user owns their name, reactions, and room membership, while the paired user gives those actions shared meaning.

This lets the app move from individual swiping to shared decisions:
two people can join the same room, compare interest through matches, and keep context about who is participating.

## Current behavior

The app stores the current user's id, name, and room code in local storage.
The remote user record stores a user identifier, room membership, and optional display name.

- Saving a name for the first time creates a room, creates a user in that room, and stores the generated user id locally.
- Name capture is currently part of entry/onboarding when no local user name exists.
- The current room is derived from the current user's room membership.
- Joining a pair updates the current user's room to the room found by the entered room code.
- A user cannot join their own current room code.
- Pairing is inferred from room membership: a room is paired when it contains more than one user.
- The paired user is the first user in the same room whose id differs from the current user id.

Room codes are generated when a new user is created.
The app creates four-character room codes from a shared counter.

## Boundaries

Do not treat the user domain as a full authentication account.
For the current domain model, the locally stored user id is the source of truth for which remote user belongs to this app install.
