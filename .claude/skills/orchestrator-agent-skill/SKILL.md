---
name: orchestrator-agent-skill
description: "Coordinate the full SDLC workflow by invoking the repository's custom phase agents in sequence, validating each artifact before continuing, and stopping on blockers."
---

# Orchestrator Agent Skill

## Goal
Run the full QA automation and delivery workflow in strict sequence by coordinating the repository's existing custom agents, validating each phase output, and preserving artifact traceability across `agent-output/`.

## Security Framework
This workflow enforces mandatory security guardrails defined in `.claude/settings.json` (`PreToolUse` / `PostToolUse` hooks backed by `.claude/hooks/security-guardrails.sh`). These hooks run automatically around every tool call. If a hook blocks a call, stop immediately, report the exact block reason, and ask the user how to proceed.

## Rules
1. Read `CLAUDE.md` once at the start of the workflow and treat it as the governing repository instruction set for every phase.
2. Invoke the named custom agent for each phase in the exact order defined below.
3. Do not skip phases.
4. Start a phase only after the previous phase completes and its required output artifact exists and is materially complete.
5. Ask clarification questions one at a time whenever required data is missing or ambiguous.
6. Do not invent business, architecture, implementation, test, or PR details.
7. Persist all phase outputs under `agent-output/`.
8. Preserve artifact traceability across this chain:
   `requirements.md -> architecture.md -> design-review.md -> impl-plan.md -> create-impl.md -> review-comment.md -> test_cases.md -> automation_test_results.md -> pr_output.md`
9. Retry a failed phase at most once when the failure appears transient. If the retry fails, stop and report the workflow failure.

## Execution Order
Run these phases in this exact order:
1. `read-user-story`
2. `create-architecture`
3. `design-reviewer`
4. `implementation-plan`
5. `create-implementation`
6. `code-reviewer`
7. `test-case-creation`
8. `automation-test-cases`
9. `pull-request-creator`

## Phase Contract

### Phase 1: Read User Story
Agent: `read-user-story`

Input:
- Jira user story ID, or equivalent kickoff context supplied by the user

Required output:
- `agent-output/requirements.md`

Required content checks:
- Description
- Functional Requirements
- Non-Functional Requirements
- Acceptance Criteria
- Business Rules

### Phase 2: Create Architecture
Agent: `create-architecture`

Input:
- `agent-output/requirements.md`

Required output:
- `agent-output/architecture.md`

Required content checks:
- Architecture recommendation
- Technology choices
- Key components and responsibilities
- Data flow
- Component diagram or equivalent component relationship description

### Phase 3: Design Review
Agent: `design-reviewer`

Input:
- `agent-output/architecture.md`

Required output:
- `agent-output/design-review.md`

Required content checks:
- Review findings
- Risks and gaps
- Agreed design decisions
- Required architecture updates or explicit confirmation that none are needed
- Review outcome

### Phase 4: Implementation Plan
Agent: `implementation-plan`

Input:
- `agent-output/architecture.md`
- `agent-output/design-review.md`

Required output:
- `agent-output/impl-plan.md`

Required content checks:
- Implementation scope
- Dependency-ordered task list
- Blocked tasks
- Recommended execution order

### Phase 5: Create Implementation
Agent: `create-implementation`

Input:
- `agent-output/impl-plan.md`
- `agent-output/requirements.md`
- `CLAUDE.md`

Required output:
- Repository implementation changes as needed
- `agent-output/create-impl.md`

Required content checks:
- Scope implemented
- Validation performed
- Remaining blockers
- Local run summary

### Phase 6: Code Review
Agent: `code-reviewer`

Input:
- `agent-output/create-impl.md`
- `.claude/codereview-checklist.md`

Required output:
- `agent-output/review-comment.md`

Required content checks:
- Review scope
- Executive summary
- Findings

Gate:
- Do not continue while unresolved Critical or High findings remain unless the user explicitly accepts that risk.

### Phase 7: Create Test Cases
Agent: `test-case-creation`

Input:
- `agent-output/requirements.md`

Required output:
- `agent-output/test_cases.md`

Required coverage checks:
- Positive test cases
- Negative test cases
- Edge test cases
- UI validation test cases
- Clear Given/When/Then structure

### Phase 8: Execute Automation Test Cases
Agent: `automation-test-cases`

Input:
- `agent-output/test_cases.md`
- `CLAUDE.md`

Required output:
- `agent-output/automation_test_results.md`

Required checks:
- Latest-run-only execution evidence
- Smoke and regression tagging coverage
- Totals and pass/fail/skip metrics
- Extent report URL or path

### Phase 9: Raise PR
Agent: `pull-request-creator`

Input:
- All generated and modified repository files
- Prior `agent-output/` artifacts
- Current git workspace state

Required output:
- `agent-output/pr_output.md`

## Orchestration Procedure
1. Read `CLAUDE.md` once.
2. Determine whether the user provided a Jira story ID or free-form kickoff instructions.
3. Start Phase 1 with the provided context.
4. After each phase, verify that the required output file exists and contains the required sections for that phase.
5. If an artifact is missing, incomplete, or contradicts the previous phase, stop and ask the next required clarification question.
6. Continue to the next phase only after the current phase passes its checks.
7. Preserve the true state of generated evidence. Never replace a failing review or test result with stale passing output.

## Failure Handling
If any phase fails:
1. Capture the exact error.
2. Retry the same phase once.
3. If the retry fails, stop and return this report:

```md
## Workflow Failure
Phase: <phase>
Error: <details>
Resolution Needed: <action>
```

If a security hook blocks a tool call:
1. Report the exact block reason.
2. Do not attempt to bypass or disable the hook.
3. Ask the user how to proceed.

## Final Success Report
When all phases complete, return:

```md
# Workflow Completed Successfully
Artifacts Generated under agent-output:
- requirements.md
- architecture.md
- design-review.md
- impl-plan.md
- create-impl.md
- review-comment.md
- test_cases.md
- automation_test_results.md
- pr_output.md
Summary:
- Total test cases created
- Total automation scripts generated
```