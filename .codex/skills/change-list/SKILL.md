---
name: change-list
description: Create concise user-facing change lists or release notes from git branch diffs in autonomous YOLO mode. Use for summaries between main/dev/develop branches, changelist review, turning commits into user-visible bullets, combining linked features, separating minor fixes, or shortening release note bullets. The skill must produce a final change list with no clarification prompts.
---

# Change List

## Workflow

Use this workflow to turn branch history into a short user-facing change list. Prefer user-visible behavior over implementation details. Run autonomously from invocation to result with no clarification prompts.

1. Compare new develop changes against the last release.
   - Always start from `origin/main..origin/develop`: `origin/main` is the last release and `origin/develop` contains the new changes.
   - Run `git status --short --branch` to see the active branch and unrelated local files.
   - Run `git log --oneline origin/main..origin/develop` for commit intent.
   - Run `git diff --stat origin/main..origin/develop` and `git diff --name-status origin/main..origin/develop` for scope.
   - When the remote refs look stale or missing, inspect branches with `git branch -a --list`, choose the closest available `main` and `develop` refs, and continue.
   - Ignore untracked build outputs and local artifacts.

2. Read targeted diffs.
   - Open only files needed to understand user-visible changes.
   - Prioritize screens, strings, navigation, domain use cases, and data flows that affect behavior.
   - Treat docs, tests, refactors, DI moves, dependencies, and tooling as supporting context.

3. Group linked changes.
   - Combine commits that belong to one feature into one bullet.
   - Example: a filter button, filter screen, genre search, saved room filters, and filtered recommendations are one user feature.
   - Put small bug fixes and polish into `Minor fixes and improvements`.
   - Do not list features already present in the comparison base.

4. Draft user-facing bullets.
   - Use plain product language.
   - Avoid class names, package names, database names, dependency names, and branch mechanics.
   - Prefer outcomes: `Added saved, searchable genre filters` instead of `Added FilterViewModel and Supabase room filter`.
   - Merge linked points before responding.
   - Keep each bullet 3-10 words.
   - Return only the final list.

## Output Example

Short release notes:

```markdown
- Added news feed
- Updated selection mechanism
- Minor fixes and improvements
```
