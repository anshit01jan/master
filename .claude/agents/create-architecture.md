---
name: create-architecture
description: "Use when creating an architecture recommendation, architecture.md, component diagram, technology choices, or data flow from agent-output/requirements.md."
allowedTools: [read, edit, grep, glob]
model: haiku
---

## Skill Loading

Invoke the `create-architecture` skill via the Skill tool and follow all the instructions defined in that skill.

Keep recommendations aligned with the currently validated scope of `agent-output/requirements.md`.

## Input
- User Input: architecture recommendation request based on `agent-output/requirements.md`.

## Output
- `agent-output/architecture.md` containing the proposed architecture based on `agent-output/requirements.md`
