---
name: create-implementation
description: "Use when implementing the application from impl.md and requirements.md, creating a localhost web app with the specified tech stack, and writing a short execution summary to agent-output/create-impl.md."
---

# Create Implementation Skill

## Goal
Implement the application and technology stack defined by `agent-output/impl-plan.md` and `agent-output/requirements.md`,create a web application that runs on localhost, and write a short execution summary to `agent-output/create-impl.md`.

## Rules
- Read the full `agent-output/impl-plan.md` file before making implementation changes.
- Read the full `agent-output/requirements.md` file before making implementation changes.
- Implement only what is supported by the implementation plan and requirements.
- Do not take assumptions on your own.
- Always ask the user explicitly if any clarification is required before proceeding on an ambiguous or blocked point.
- Use the technology stack stated in `agent-output/impl-plan.md`.
- The delivered result must be a web application that can run on localhost.
- Keep implementation decisions traceable to the requirements and implementation plan.
- Create or update `agent-output/create-impl.md` with a short summary of what was implemented.

## Process
1. Read `agent-output/impl-plan.md` completely.
2. Read `agent-output/requirements.md` completely.
3. Identify the required stack, implementation scope, dependencies, and any blocked or unresolved decisions.
4. If any ambiguity or blocker remains, ask the user explicitly before proceeding.
5. Implement the application in dependency order as defined by `agent-output/impl-plan.md`.
6. Ensure the application can be started locally on localhost.
7. Validate the implementation with the narrowest available checks.
8. Create or update `agent-output/create-impl.md` with a short summary of completed work, validation performed, and any remaining blockers. This summary must reflect the actual final state of the code, not an aspirational or partially-rolled-back state.

## Output
Create or update `agent-output/create-impl.md` with these sections:
- Scope Implemented
- Validation Performed
- Remaining Blockers
- Local Run Summary

## Failure Handling
- If `agent-output/impl.md` is missing, report the exact blocker.
- If `agent-output/requirements.md` is missing, report the exact blocker.
- If the stack or scope is ambiguous, ask the user explicitly before proceeding.
