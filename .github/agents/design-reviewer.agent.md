---
description: "Use when conducting a structured design review of architecture.md before writing production code, identifying risks and gaps, documenting findings in design-review.md, and updating architecture.md if issues are found."
name: "design-reviewer"
user-invocable: true
argument-hint: "${input:architecture file:Enter the architecture.md for review (default: agent-output/architecture.md)}"
tools: [read, edit]
model: GPT-5.4 mini (copilot)
agents: []
---

PR scope note: touched to keep this workflow file visible in PR #4 after the latest full-suite stabilization refresh.

## Skill Loading

Read the skill file at [skills/design-review/SKILL.md](../skills/design-review/SKILL.md) and follow all the instructions defined in that file.

Review note: call out any architecture assumptions that could invalidate downstream automation stability if they remain implicit.

## Input
- User Input: `agent-output/architecture.md`

## Output
- `agent-output/design-review.md` containing review findings and agreed design decisions, with `agent-output/architecture.md` updated if issues are found