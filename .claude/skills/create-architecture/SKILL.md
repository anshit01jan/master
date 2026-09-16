---
name: create-architecture
description: "Use when creating an architecture recommendation, architecture.md, component diagram, technology choices, or data flow from agent-output/requirements.md."
---

# Create Architecture Skill

## Goal
Read the full `agent-output/requirements.md` file and produce a clear, concise, and actionable `agent-output/architecture.md` document.

## Rules
- Read the entire `agent-output/requirements.md` file before making recommendations.
- Base the architecture on the requirements only.
- Do not invent business requirements that are not present in the requirements file.
- Keep recommendations practical and implementation-ready.
- Be clear and concise.
- Ask exactly one clarification question at a time only when a missing detail blocks a meaningful architecture recommendation.
- Always reference specific requirements when justifying architectural decisions.
- Recommend a technology stack aligned with the stated non-functional requirements (performance, scalability, security).
- Justify key architectural decisions with clear rationale.

## Process
1. Read the complete `agent-output/requirements.md` file to extract key functional and non-functional requirements.
2. Break down the system into logical, independent components based on responsibility.
3. Specify how components communicate and exchange data.
4. Map end-to-end data flow from user input through system processing to output.
5. Recommend specific technology choices aligned with requirements.
6. Create or update `agent-output/architecture.md`.

## Output
Create or update `agent-output/architecture.md` with these sections:
- Overview
- Architecture Recommendation
  - High-level architecture style (e.g., Layered, Microservices, Event-Driven, MVC) with justification.
- Key Components and Responsibilities
  - For each component: name, responsibility, interfaces, and dependencies.
- Data Flow
  - End-to-end flow (user request -> processing -> response), data formats, and integration points.
- Component Diagram
  - At least one Mermaid component or data-flow diagram with clearly labeled components, interfaces, and dependencies.

## Output Quality
- Keep the document structured and concise.
- Use testable and traceable statements tied to the requirements.
- Prefer specific recommendations over broad option lists.
- Explain why each major component exists.
- Component responsibilities must be clear and non-overlapping.
- The architecture must be ready for the design review phase before any implementation begins.

## Failure Handling
- If `agent-output/requirements.md` is missing, report the exact blocker.
- If requirements are too ambiguous to recommend an architecture, ask exactly one blocking clarification question at a time.
