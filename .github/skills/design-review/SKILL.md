---
name: "design-review"
description: "Use when conducting a structured design review of architecture.md before writing production code, identifying risks and gaps, documenting findings in design-review.md, and updating architecture.md if issues are found."
---

PR scope note: touched to keep this skill visible in PR #4 after the latest full-suite stabilization refresh.

# Design Review Skill

## Goal
Conduct a structured design review of `agent-output/architecture.md` before any production code is written, document findings in `agent-output/design-review.md`, and update `agent-output/architecture.md` if concrete issues are found.

Risk note: highlight assumptions that could later surface as automation setup or rerun instability.

## Rules
- Read the full `agent-output/architecture.md` file before making any recommendation.
- Read `agent-output/requirements.md` when available to verify requirement-to-architecture alignment.
- Focus on architecture quality, design risks, missing decisions, security gaps, scalability concerns, maintainability issues, operational blind spots, and requirement mismatches.
- Do not write production code.
- Do not invent requirements that are not supported by the available documents.
- Update `agent-output/architecture.md` only when you identify a concrete issue that should be corrected in the architecture.
- Keep the review structured, concise, and decision-oriented.
- Ask exactly one clarification question at a time only if a blocking ambiguity prevents a meaningful review.

## Process
1. Read the complete `agent-output/architecture.md` file.
2. Read `agent-output/requirements.md` when available to verify alignment between requirements and architecture.
3. Review the architecture as a senior reviewer before implementation and identify:
   - requirement coverage gaps
   - security and compliance risks
   - operational and observability gaps
   - scalability and reliability concerns
   - unclear or missing design decisions
4. If concrete issues are found, update `agent-output/architecture.md` with the necessary corrections or clarifications.
5. Create or update `agent-output/design-review.md` with the findings and agreed design decisions.

## Output
Create or update `agent-output/design-review.md` with these sections:
- Review Scope
- Findings
- Risks and Gaps
- Agreed Design Decisions
- Required Architecture Updates
- Review Outcome

## Review Outcome Rules
- If no material issues are found, state that explicitly in `Review Outcome` and record any residual risks.
- If issues are found and architecture is updated, summarize exactly what changed in `Required Architecture Updates`.

## Failure Handling
- If `agent-output/architecture.md` is missing, report the exact blocker.
- If a blocking ambiguity prevents a meaningful review, ask exactly one clarification question at a time.