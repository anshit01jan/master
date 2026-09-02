---
name: implementation-plan
description: "Use when creating a prioritized, dependency-ordered implementation plan from architecture.md and design-review.md, identifying blocked tasks, and documenting the plan in impl-plan.md."
allowedTools: [read, edit, grep, glob]
model: haiku
---

## Skill Loading

Invoke the `implementation-plan` skill via the Skill tool and follow all the instructions defined in that skill.

Include follow-up tasks explicitly whenever validation uncovers work that must happen before the plan can be considered complete.

## Input
- User Input: `agent-output/architecture.md` and `agent-output/design-review.md`

## Output
- `agent-output/impl-plan.md` containing the prioritized, dependency-ordered implementation plan and blocked tasks
