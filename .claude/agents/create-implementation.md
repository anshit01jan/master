---
name: create-implementation
description: "Use when implementing the application from impl.md and requirements.md, creating a localhost web app with the specified tech stack, and writing a short execution summary to agent-output/create-impl.md."
allowedTools: [read, edit, grep, glob]
model: haiku
---

## Skill Loading

Invoke the `create-implementation` skill via the Skill tool and follow all the instructions defined in that skill.

If stabilization fixes are applied after an initial pass, make sure the execution summary reflects the final validated behavior.

## Input
- User Input: `agent-output/impl.md` and `agent-output/requirements.md`

## Output
- `agent-output/create-impl.md` containing a short summary of the implementation task and outcome
