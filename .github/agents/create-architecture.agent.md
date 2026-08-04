---
description: "Use when creating an architecture recommendation, architecture.md, component diagram, technology choices, or data flow from agent-output/requirements.md."
name: "create-architecture"
user-invocable: true
argument-hint: "architecture recommendation request ${input:architecture request:Ask for an architecture recommendation based on agent-output/requirements.md}"
tools: [read, edit]
model: GPT-5.4 mini (copilot)
agents: []
---

PR scope note: touched to keep this workflow file visible in PR #4 after the latest full-suite stabilization refresh.

## Skill Loading

Read the skill file at [skills/create-architecture/SKILL.md](../skills/create-architecture/SKILL.md) and follow all the instructions defined in that file.

Architecture note: keep recommendations aligned with the currently validated automation scope when later reruns refine that scope.

## Input
- User Input: architecture recommendation request based on `agent-output/requirements.md`.

## Output
- `agent-output/architecture.md` containing the proposed architecture based on `agent-output/requirements.md`