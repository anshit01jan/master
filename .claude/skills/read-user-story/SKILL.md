---
name: read-user-story
description: "Use when extracting clear functional and non-functional requirements from a Jira user story by story ID, with one-at-a-time clarifications and concise output."
---

# Read User Story Skill

## Goal
Fetch a Jira user story by ID and produce a clear, concise, and finalized `agent-output/requirements.md` with explicit requirements only.

## Tone & Style
- **Direct & Concise**: Ask clarification questions one at a time, avoiding overwhelming the user with multiple questions.
- **Evidence-Based**: Always reference specific text from the Jira story when clarifying.
- **Progressive**: Build requirements incrementally, validating each section before moving to the next.
- **Non-Assumptive**: Do not invent details; always ask rather than assume.

## Rules
- Validate the input story ID format before processing.
- Read the entire user story content available from Jira.
- Do not assume missing details.
- Ask one clarification question at a time when ambiguity exists.
- Do not finalize output until blocking ambiguities are resolved.
- Keep requirements clear, concise, relevant, and testable.
- Avoid implementation design unless explicitly present in the story.
- Do not invent business rules or non-functional constraints.
- Preserve any stated acceptance-criteria gaps so later phases can distinguish missing scope from implementation defects, rather than silently filling them in.

## Process
1. Validate story ID format (for example: PROJ-123).
2. Fetch story details using the Atlassian/Jira MCP tools available in this workspace (e.g. `jira_get_issue`, `jira_search`).
3. Read all relevant fields from the story:
   - Summary
   - Description
   - Acceptance Criteria
   - Comments (if relevant to requirements)
   - Linked context if available and relevant
4. Create a draft requirements document with only these sections:
   - Description
   - Functional Requirements
   - Non-Functional Requirements
   - Acceptance Criteria
   - Business Rules
5. Identify missing, ambiguous, or contradictory points.
6. Ask exactly one clarification question at a time.
7. After each user response, update the draft and re-check for remaining ambiguity.
8. Finalize when no unresolved ambiguity remains.

## Clarification Question Examples
- "The story mentions 'validate email'. Should this accept [specific format]? Any blocklist or allowlist?"
- "For the timeout mentioned as 'quick response', what is the acceptable range in milliseconds?"
- "Should this feature work offline, or require internet connectivity?"
- "Are there any audit or logging requirements for this action?"

## Output
- Create or update `agent-output/requirements.md` in markdown.
- Use Markdown format with clear section headers (##, ###).
- Keep functional requirements in active voice (e.g., "The system shall validate...").
- Acceptance criteria must be testable and measurable, one per line as a bulleted list.
- Business rules must be numbered for traceability.
- Each section must have at least one entry; flag missing sections explicitly.

## Success Criteria
- `agent-output/requirements.md` is complete, unambiguous, and ready for the architecture phase.
- The user has explicitly confirmed all clarifications.
- All sections are populated with concrete, actionable content.

## Failure Handling
- If the Jira story is missing, inaccessible, or invalid, report the exact blocker.
- Ask only for the next required input (for example, corrected story ID or access confirmation).
