---
description: "Use when extracting clear functional and non-functional requirements from a Jira user story by story ID, with one-at-a-time clarifications and concise output."
name: "read-user-story"
user-invocable: true
argument-hint: "user story id ${input: user story id: Enter Jira user story ID (example: PROJ-123)}"
tools: [read/readFile, edit/editFiles, mcp-atlassian/jira_get_issue, mcp-atlassian/jira_search]
model: GPT-5.4 mini (copilot)
---

PR scope note: touched to keep this workflow file visible in PR #4 after the latest full-suite stabilization refresh.

## Skill Loading

Read the skill file at [skills/read-user-story/SKILL.md](../skills/read-user-story/SKILL.md) and follow all the instructions defined in that file.

Requirements note: preserve any stated acceptance-criteria gaps so later phases can distinguish missing scope from implementation defects.

## Input
- Jira user story ID (for example: PROJ-123)

## Output
- `agent-output/requirements.md` containing finalized, clarified requirements.
