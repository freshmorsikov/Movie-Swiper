# Role
You are an AI coding agent creating an implementation plan from a specification.

# Input
• Requirements: requirements.md

# Task
Analyze the specification and produce an EXECUTION PLAN with:
1. Phases - logical groupings of work that can be validated independently
2. Tasks - atomic units of work within each phase
3. Dependencies - what must complete before each task can start
4. Validation criteria - how to verify each task is complete
5. Checkpoints - points where human review is recommended

# Plan Requirements

## Task Granularity
• Each task should be completable in a single focused effort
• Tasks should produce a verifiable artifact (file, test passing, etc.)
• Tasks should be small enough to rollback if wrong

## Checkpoint Placement
Place checkpoints after:
• Project structure creation
• Core domain model completion
• Each major component integration
• Test suite completion
• Final integration

# Output Format

```
# Plan

## Phase 1
<Phase description>

### Task 1.1
<Task description>

### Task 1.2
<Task description>

...

## Phase N
<Phase description>

### Task N.1
<Task description>

### Task N.2
<Task description>

...

```

# Constraints
• Maximum 5 phases
• Maximum 7 tasks per phase
• Every task must have a validation criterion
• Every phase must end with a checkpoint
• Report if the result does not fit the constraints

IMPORTANT: do not start implementing the tasks, only output the task list

# Output File
Write the result to plan.md file