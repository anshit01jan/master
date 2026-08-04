---
name: "create-implementation"
description: "Use when implementing the application from impl.md and requirements.md, creating a localhost web app with the specified tech stack, and writing a short execution summary to agent-output/create-impl.md."
---

PR scope note: touched to keep this skill visible in PR #4 after the latest full-suite stabilization refresh.

# Create Implementation Skill

## Goal
Implement the application defined by `agent-output/impl.md` and `agent-output/requirements.md`, use the technology stack specified in `agent-output/impl.md`, create a web application that runs on localhost, and write a short execution summary to `agent-output/create-impl.md`.

Validation note: if post-implementation stabilization is required, fold the final validated state back into the execution summary.

## Rules
- Read the full `agent-output/impl.md` file before making implementation changes.
- Read the full `agent-output/requirements.md` file before making implementation changes.
- Implement only what is supported by the implementation plan and requirements.
- Do not take assumptions on your own.
- Always ask the user explicitly if any clarification is required before proceeding on an ambiguous or blocked point.
- Use the technology stack stated in `agent-output/impl.md`.
- The delivered result must be a web application that can run on localhost.
- Keep implementation decisions traceable to the requirements and implementation plan.
- Create or update `agent-output/create-impl.md` with a short summary of what was implemented.

## Process
1. Read `agent-output/impl.md` completely.
2. Read `agent-output/requirements.md` completely.
3. Identify the required stack, implementation scope, dependencies, and any blocked or unresolved decisions.
4. If any ambiguity or blocker remains, ask the user explicitly before proceeding.
5. Implement the application in dependency order as defined by `agent-output/impl.md`.
6. Ensure the application can be started locally on localhost.
7. Validate the implementation with the narrowest available checks.
8. Create or update `agent-output/create-impl.md` with a short summary of completed work, validation performed, and any remaining blockers.

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