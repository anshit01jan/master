---
name: code-reviewer
description: "Use when reviewing the implementation by taking input from agent-output/create-impl.md and .claude/codereview-checklist.md, and writing agent-output/review-comment.md with findings and resolutions."
---

# Code Reviewer Skill

## Objective
Perform a structured code review of the Python + Bootstrap implementation in this framework and produce a single actionable report in `agent-output/review-comment.md`.

The review must inspect the implemented Flask application, templates, styling, and related framework configuration, then include findings with concrete resolutions.

## Mandatory Scope
Review all relevant implementation files from:
- `app.py`
- `templates/**`
- `static/css/**`
- `requirements.txt`
- `README.md`
- `scripts/**` (if part of implementation validation/reporting)

Use `agent-output/create-impl.md` as the implementation summary and `.claude/codereview-checklist.md` as the mandatory checklist.

## Review Checklist (Mandatory)
Evaluate findings against these dimensions from `.claude/codereview-checklist.md`:
1. Correctness: Does each component behave as described by `agent-output/create-impl.md`?
2. Security: Are secrets excluded from output? Are Flask form inputs validated and handled safely?
3. Error Handling: Are route failures, missing records/files, and empty-state paths handled gracefully?
4. Test Coverage: Do tests (if present) cover happy paths and missing-field/not-found edge cases?
5. Code Clarity: Are function names, route handlers, and template logic easy to follow without extra comments?
6. DRY Principle: Is duplicated logic identified with a concrete shared refactor path?
7. Dependency Safety: Are dependency risks or known-vulnerable versions flagged from `requirements.txt` or other visible manifests?

## Hard Constraints
- Do not modify source code during review.
- Do not invent findings without file-level evidence.
- Do not provide generic recommendations without specific file references.
- Report only required changes in `agent-output/review-comment.md`.
- Every finding must include a concrete resolution.
- If a checklist category has no issues, explicitly state that there are no required changes.
- Do not list files that have no review comments.
- Present file-specific required changes in tabular format.
- Never soften, omit, or reclassify a real finding in order to make the reviewed implementation or a related automation run look more successful than it is.

## Execution Flow
1. Read `agent-output/create-impl.md` and `.claude/codereview-checklist.md` before reviewing code.
2. Discover files in the mandatory scope for this Flask + Bootstrap framework.
3. Read and analyze the implementation against the checklist.
4. Identify only required changes and classify by severity:
   - Critical
   - High
   - Medium
   - Low
5. For each finding, include:
   - File name
   - Issue
   - Impact
   - Resolution
6. Produce a concise file-by-file review summary only for files that require changes.
7. Create or update `agent-output/review-comment.md`.
