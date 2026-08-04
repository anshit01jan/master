---
name: "read-user-story"
description: "Use when extracting clear functional and non-functional requirements from a Jira user story by story ID, with one-at-a-time clarifications and concise output."
---

PR scope note: touched to keep this skill visible in PR #4 after the latest full-suite stabilization refresh.

# Read User Story Skill

## Goal
Fetch a Jira user story by ID and produce a clear, concise, and finalized `agent-output/requirements.md` with explicit requirements only.

Traceability note: preserve missing-detail callouts so later phases do not overstate requirement certainty.

## Rules
- Validate the input story ID format before processing.
- Read the entire user story content available from Jira.
- Do not assume missing details.
- Ask one clarification question at a time when ambiguity exists.
- Do not finalize output until blocking ambiguities are resolved.
- Keep requirements clear, concise, relevant, and testable.
- Avoid implementation design unless explicitly present in the story.
- Do not invent business rules or non-functional constraints.

## Process
1. Validate story ID format (for example: PROJ-123).
2. Fetch story details using Jira MCP tools:
   - `mcp-atlassian/jira_get_issue`
   - `mcp-atlassian/jira_search` (if lookup support is needed)
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

## Output
- Create or update `agent-output/requirements.md` in markdown.

## Failure Handling
- If the Jira story is missing, inaccessible, or invalid, report the exact blocker.
- Ask only for the next required input (for example, corrected story ID or access confirmation).
