---
name: pull-request-creation
description: "Use when creating a Pull Request for changed files in the workspace, including PR description, changelog entry, and reviewer checklist."
---

# Pull Request Creation Skill

## Scope

- Detect new and modified files in the current workspace.
- Include all the files present in `.claude/agents`, `.claude/skills`, `CLAUDE.md`, `.claude/commands`, and `.claude/settings.json` in the PR.
- Check for `src` folder and include all the files in the PR, if they are not committed yet.
- Also include `pom.xml` and `testng.xml` if they are not committed yet.
- Ask the user to approve the list of files to be committed.
- When an existing feature-branch PR is being reused, the PR must always reflect the true state of the latest automation run and code review. Never carry forward a passing evidence snapshot that hides or replaces a real, still-unresolved failure.

## Constraints

- Always use the GitHub account/credentials configured for this workspace.
- Do not commit without explicit user approval.
- Do not push directly to `main`.
- Always use feature branches for PR source branches.
- Ask the user for the commit message; do not invent commit messages.
- Prefer the GitHub MCP server's tools for repository, branch, commit, push, and PR creation; fall back to the `gh` CLI via Bash if the GitHub MCP server is not configured.
- Always ask one question at a time for user approval before proceeding to the next step.

## Required PR Description Sections

The generated PR description must include all of the following sections:

- Summary — 2-3 sentence overview of what was built and why.
- Changes Made — bulleted list of all files added/modified and the reason for each.
- Test Evidence — paste the actual test run output or link to CI results, exactly as produced by the latest run.
- Known Limitations — anything marked 'Not Found' or out of scope, including any known failing or skipped tests.
- Reviewer Checklist — a tick-list the reviewer must complete before approving.

## Approach

1. Analyze repository state:
   - Detect unstaged, staged, new, modified, and deleted files.
   - Categorize changed files clearly.

2. Ask the user to select a branch strategy:
   - Create a new feature branch, or
   - Use an existing feature branch.
   - Verify branch existence when using an existing branch.

3. Ask the user to review the list of files and approve staging:
   - Show all detected new and modified files.
   - Ask explicitly: "Review the files below and choose an option."
   - Options:
     - Approve: Create commit and push all the files.
     - Modify: Add or remove files from staging.
     - Cancel: Stop without committing.

4. Request commit approval:
   - Confirm the final files to be committed.
   - Confirm the commit message with the user.
   - Use the approved commit message for the commit and PR title.

5. Create the commit and push.

6. Create the Pull Request:
   - Use the branch as source and `main` as target.
   - Generate a PR description containing all required sections.

7. Report results:
   - Return the GitHub PR link as the final output.

## Output Format

Return the final response with the PR link only, plus a concise summary of the final PR details.

Example:

```
# Pull Request Created Successfully

**PR URL:** https://github.com/<owner>/<repo>/pull/<number>
**Source Branch:** [branch-name]
**Target Branch:** main
**Commit SHA:** [abbreviated SHA]

**Files Changed:** [count]
- New Files: [count]
- Modified Files: [count]

```

## Failure Handling

- If branch creation fails, report exact error and ask the user to verify the branch name and whether it already exists.
- If commit fails, report exact error and ask the user to verify the selected files and commit message.
- If push fails, report the exact error and ask the user to verify GitHub access and network.
- If PR creation fails, report the exact error and provide manual PR creation fallback steps.
