---
name: read-user-story
description: "Use when extracting clear functional and non-functional requirements from a Jira user story by story ID, with one-at-a-time clarifications and concise output."
allowedTools: [read, mcp-atlassian]
model: haiku
---

## Skill Loading

Invoke the `read-user-story` skill via the Skill tool and follow all the instructions defined in that skill.

Use the Atlassian/Jira MCP tools configured for this workspace (see `.mcp.json`, server `mcp-atlassian`) to fetch the story.

Preserve any stated acceptance-criteria gaps so later phases can distinguish missing scope from implementation defects.

## Input
- Jira user story ID (for example: PROJ-123)

## Output
- `agent-output/requirements.md` containing finalized, clarified requirements.
