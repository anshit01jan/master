---
name: design-review
description: "Use when conducting a structured design review of architecture.md before writing production code, identifying risks and gaps, documenting findings in design-review.md, and updating architecture.md if issues are found."
---

# Design Review Skill

## Goal
Conduct a structured design review of `agent-output/architecture.md` before any production code is written, document findings in `agent-output/design-review.md`, and update `agent-output/architecture.md` if concrete issues are found.

## Tone & Style
- **Critical & Thorough**: Challenge architectural decisions and probe for weaknesses.
- **Evidence-Based**: Reference specific requirements, standards, and best practices.
- **Balanced**: Acknowledge strengths while identifying improvement areas.
- **Constructive**: Frame findings as opportunities for improvement, not criticisms.
- **Collaborative**: Suggest mitigation strategies and remediation approaches.

## Rules
- Read the full `agent-output/architecture.md` file before making any recommendation.
- Read `agent-output/requirements.md` when available to verify requirement-to-architecture alignment.
- Focus on architecture quality, design risks, missing decisions, security gaps, scalability concerns, maintainability issues, operational blind spots, and requirement mismatches.
- Do not write production code.
- Do not invent requirements that are not supported by the available documents.
- Update `agent-output/architecture.md` only when you identify a concrete issue that should be corrected in the architecture.
- Keep the review structured, concise, and decision-oriented.
- Ask exactly one clarification question at a time only if a blocking ambiguity prevents a meaningful review.
- Call out any architecture assumptions that would invalidate downstream automation or implementation stability if left implicit.

## Review Criteria & Validation Checklist

### 1. Requirements Alignment
- All functional requirements are addressed by components/flows.
- All non-functional requirements are explicitly mapped to architectural decisions.
- Scalability approach supports projected growth.

### 2. Architecture Principles (SOLID & Best Practices)
- Follow SOLID principles for component design.
- Follow DRY (Don't Repeat Yourself) to avoid duplication.
- Follow KISS (Keep It Simple, Stupid) to avoid unnecessary complexity.

### 3. Component Design
- Each component has a clear, single responsibility.
- Component boundaries are well-defined.
- Dependencies between components are minimal and explicit.
- No circular dependencies or tight coupling.
- Interfaces are clear and well-specified.

### 4. Data Flow & Integration
- End-to-end data flows are complete and clear.
- Data transformations are well-documented.

### 5. Technology Stack
- Each technology choice is justified.
- Technologies integrate well without conflicts.

### 6. Non-Functional Requirements
- Performance: Caching, indexing, query optimization strategies are sound.
- Scalability: Horizontal/vertical scaling is feasible and cost-effective.
- Availability: Redundancy, failover, and disaster recovery are planned.

### 7. Documentation & Communication
- Architecture is well-documented with clear sections.
- Component responsibilities are clearly stated.
- Data flows are easy to follow.

## Risk Categories & Severity Levels

### High Risk (Must Address Before Implementation)
- Architectural decisions conflict with requirements.
- Non-functional requirements cannot be met.
- Critical dependencies are unclear or unstable.

### Medium Risk (Address Before Implementation, May Propose Workaround)
- Component boundaries are ambiguous.
- Technology choices lack community support or team expertise.
- Scalability approach has questionable feasibility.

### Low Risk / Observations (Address in Future Iterations)
- Minor performance optimizations are possible.
- Optional components could improve maintainability.

## Finding Documentation Format

For each finding, specify:

```
### Finding: [Title]
- Severity: High / Medium / Low
- Category: [Component Design / Security / Performance / etc.]
- Issue: [Clear description of the problem]
- Impact: [What happens if not addressed]
- Recommendation: [Specific suggested remediation]
- Effort: [Estimate: Low / Medium / High]
- Related Requirement(s): [Link back to specific requirements]
```

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
  - Total findings by severity (High / Medium / Low).
  - Distribution by category (Component Design, Security, Performance, etc.).
  - Overall completeness and alignment score.
- Risks and Gaps
  - Critical risks that could block implementation.
  - Gaps in architecture that may lead to future issues.
  - Mitigation strategies for each risk.
- Agreed Design Decisions
  - Design decisions that are accepted and agreed upon, with rationale.
- Required Architecture Updates
  - Specific updates to `architecture.md` before implementation, with clear instructions for each.
- Review Outcome
  - Overall outcome of the design review, and whether the architecture is approved for implementation or requires revisions.

## Review Outcome Rules
- If no material issues are found, state that explicitly in `Review Outcome` and record any residual risks.
- If issues are found and architecture is updated, summarize exactly what changed in `Required Architecture Updates`.

## Failure Handling
- If `agent-output/architecture.md` is missing, report the exact blocker.
- If a blocking ambiguity prevents a meaningful review, ask exactly one clarification question at a time.
