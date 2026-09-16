---
description: "Run the full QA automation and delivery workflow end-to-end as a master orchestrator across requirement enrichment, architecture, design review, implementation planning, implementation, code review, test case creation, automation execution, and pull request creation."
argument-hint: "[optional: Jira story ID or free-form kickoff instructions]"
---

Invoke the custom agent `orchestrator-agent` and pass through the user's optional Jira story ID or kickoff instructions.

`orchestrator-agent` is responsible for loading the `orchestrator-agent-skill`, running every SDLC phase in order, validating the artifact chain under `agent-output/`, and either returning the next blocking clarification question or the final workflow summary.

Do not duplicate the orchestration logic in this command file.
