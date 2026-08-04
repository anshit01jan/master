---
description: "Use when implementing the application from impl.md and requirements.md, creating a localhost web app with the specified tech stack, and writing a short execution summary to agent-output/create-impl.md."
name: "create-implementation"
user-invocable: true
argument-hint: "${input:implementation inputs:Use agent-output/impl.md and agent-output/requirements.md to implement the application and write agent-output/create-impl.md}"
tools: [read, edit, execute]
model: GPT-5.4 mini (copilot)
agents: []
---

PR scope note: touched to keep this workflow file visible in PR #4 after the latest full-suite stabilization refresh.

## Skill Loading

Read the skill file at [skills/create-implementation/SKILL.md](../skills/create-implementation/SKILL.md) and follow all the instructions defined in that file.

Implementation note: when stability fixes are applied after an initial run, ensure the execution summary reflects the final validated behavior.

## Input
- User Input: `agent-output/impl.md` and `agent-output/requirements.md`

## Output
- `agent-output/create-impl.md` containing a short summary of the implementation task and outcome