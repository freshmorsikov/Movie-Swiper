---
name: commit-push
description: Verify repository Definition of Done, inspect local changes, commit only intended work, and push the current branch. Use when the user asks Codex to commit, commit and push, push changes, ship the current work, or finish a completed code change in this project.
---

# Commit Push

## Workflow

Use this workflow to turn completed local work into a pushed commit without sweeping in unrelated workspace state.

1. Read the active repository instructions before committing.
   - Check `AGENTS.md` and any nested instruction files that apply to changed files.
   - Identify the repository Definition of Done and required verification commands.

2. Find and classify changes.
   - Run `git status --short`.
   - Review diffs for tracked files with `git diff` and, if anything is already staged, `git diff --cached`.
   - Treat untracked files as unrelated unless the user explicitly requested them or they are clearly part of the completed task.
   - Do not revert, delete, or modify unrelated changes.

3. Verify Definition of Done.
   - Run the required checks from the repository instructions, unless they were already run after the final code change in the same turn.
   - For this repository, the required build check is `./gradlew :composeApp:assembleDebug`.
   - If a required check cannot run, explain the blocker before committing unless the user explicitly asks to commit anyway.

4. Stage only intended files.
   - Use explicit paths with `git add`.
   - Re-run `git status --short` and `git diff --cached --stat`.
   - Confirm the staged diff contains only the requested work.

5. Commit.
   - Use a concise imperative commit message that describes the user-visible change.
   - After committing, capture the short hash and subject.

6. Push.
   - Push the current branch to its upstream when one exists.
   - If no upstream exists, push to `origin` with `--set-upstream` for the current branch.
   - If network or sandbox restrictions block the push, request the needed escalation and retry.

7. Report clearly.
   - State the commit hash, branch, and push target.
   - Mention any untracked or unrelated files left untouched.
   - In Codex desktop, emit git stage, commit, and push directives only after those actions actually succeeded.

## Safety Rules

- Never use broad staging such as `git add .` when unrelated files are present.
- Never use destructive cleanup commands for unrelated files as part of this skill.
- Never amend, rebase, force-push, or change branches unless the user explicitly asks.
- If the final verification fails because of the new work, fix the issue before committing.
