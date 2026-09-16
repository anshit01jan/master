---
name: implementation-plan
description: "Use when creating a prioritized, dependency-ordered implementation plan from architecture.md and design-review.md, identifying blocked tasks, and documenting the plan in impl-plan.md."
---

# Implementation Plan Skill

## Goal
Break the approved architecture down into a prioritized, dependency-ordered task list, identify blocked tasks that cannot start until other tasks finish, and document the plan in `agent-output/impl-plan.md`.

## Rules
- Read the full `agent-output/architecture.md` file before creating the plan.
- Read the full `agent-output/design-review.md` file before creating the plan.
- Base the plan on the approved architecture and review decisions only.
- Create a plan that is based on Python framework and Bootstrap for the frontend, unless the architecture or review documents specify otherwise.
- Do not invent scope that is not supported by the architecture or review documents.
- Order tasks by dependency first, then by implementation priority.
- Keep tasks implementation-oriented, specific, and actionable.
- Keep the plan structured, clear and concise.
- Ask exactly one clarification question at a time only if a blocking ambiguity prevents a meaningful plan.
- Implementation plan should be actionable and ready for execution by a development team.
- Implementation Plan should be for a simple login web application.

## Process
1. Read `agent-output/architecture.md` completely.
2. Read `agent-output/design-review.md` completely.
3. Extract the implementation-relevant components, interfaces, policies, integrations, and open decisions.
4. Break the work into concrete tasks grouped in dependency order.
5. Prioritize tasks within that dependency order so foundational work appears before dependent work.
6. Mark any task that cannot start until another task finishes or until an open decision is resolved.
7. Create or update `agent-output/impl-plan.md`.

## Technology Stack
- Backend: Flask
- Frontend: Bootstrap
- Templates: Jinja2
- Runtime: Python on localhost
- Persistence: In-memory application store for localhost execution and deterministic workflow validation

## Implementation Constraints
- Follow the approved architecture and design-review decisions.
- Store passwords as salted one-way hashes.
- Generate high-entropy single-use reset tokens.
- Keep authentication, lockout, and token state centralized and testable.
- Do not implement unrelated platform or admin features.

## Output
Create or update `agent-output/impl-plan.md` with these sections:
- Plan Scope
- Technology Stack
- Dependency-Ordered Task List
- Recommended Execution Order

## Output Quality
- Use clear task names.
- State dependencies explicitly.
- Prefer small, implementation-ready tasks over broad phases.
- Make blocking conditions unambiguous.
- Ensure the task order is traceable to the architecture and design review.

## Failure Handling
- If `agent-output/architecture.md` is missing, report the exact blocker.
- If `agent-output/design-review.md` is missing, report the exact blocker.
- If a blocking ambiguity prevents a meaningful plan, ask exactly one clarification question at a time.
