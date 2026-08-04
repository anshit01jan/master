---
name: "create-architecture"
description: "Use when creating an architecture recommendation, architecture.md, component diagram, technology choices, or data flow from agent-output/requirements.md."
---

PR scope note: touched to keep this skill visible in PR #4 after the latest full-suite stabilization refresh.

# Create Architecture Skill

## Goal
Read the full `agent-output/requirements.md` file and produce a clear, concise, and actionable `agent-output/architecture.md` document.

Scope note: architecture output should remain compatible with the validated runnable scope documented by later automation phases.

## Rules
- Read the entire `agent-output/requirements.md` file before making recommendations.
- Base the architecture on the requirements only.
- Do not invent business requirements that are not present in the requirements file.
- Keep recommendations practical and implementation-ready.
- Be clear and concise.
- Ask exactly one clarification question at a time only when a missing detail blocks a meaningful architecture recommendation.

## Process
1. Read the complete `agent-output/requirements.md` file.
2. Extract the functional requirements, non-functional requirements, acceptance criteria, and business rules that affect architecture.
3. Propose a suitable architecture with:
   - high-level architecture style
   - key components
   - component responsibilities
   - end-to-end data flow
4. Add at least one Mermaid component or flow diagram.
5. Create or update `agent-output/architecture.md`.

## Output
Create or update `agent-output/architecture.md` with these sections:
- Overview
- Architecture Recommendation
- Key Components and Responsibilities
- Data Flow
- Component Diagram

## Output Quality
- Keep the document structured and concise.
- Use testable and traceable statements tied to the requirements.
- Prefer specific recommendations over broad option lists.
- Explain why each major component exists.

## Failure Handling
- If `agent-output/requirements.md` is missing, report the exact blocker.
- If requirements are too ambiguous to recommend an architecture, ask exactly one blocking clarification question at a time.