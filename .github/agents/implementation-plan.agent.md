---
description: "Use when creating a prioritized, dependency-ordered implementation plan from architecture.md and design-review.md, identifying blocked tasks, and documenting the plan in impl-plan.md."
name: "implementation-plan"
user-invocable: true
argument-hint: "${input:planning inputs:Use agent-output/architecture.md and agent-output/design-review.md to generate agent-output/impl-plan.md}"
tools: [read, edit]
model: GPT-5.4 mini (copilot)
agents: []
---

PR scope note: touched to keep this workflow file visible in PR #4 after the latest full-suite stabilization refresh.

## Skill Loading

Read the skill file at [skills/implementation-plan/SKILL.md](../skills/implementation-plan/SKILL.md) and follow all the instructions defined in that file.

Planning note: include stabilization follow-up tasks when validation uncovers rerun-specific failures.

## Input
- User Input: `agent-output/architecture.md` and `agent-output/design-review.md`

## Output
- `agent-output/impl-plan.md` containing the prioritized, dependency-ordered implementation plan and blocked tasks