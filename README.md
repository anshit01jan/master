# GitProject

## Overview

This repository contains a hybrid local automation project with:
- A Python Flask web application in `app/` for login, forgot password, reset password, and dashboard flows.
- A Java + Maven + Selenium + TestNG + Cucumber automation framework in `src/` for end-to-end UI validation.
- A test orchestration layer that starts the local app, resets application state, and validates automation flows against deterministic data.

## Key Technologies

- Java 17
- Maven
- Selenium WebDriver
- Cucumber JVM
- TestNG
- Extent Reports
- Python 3 / Flask
- Log4j 2

## Project Structure

Root layout:

- `.readme` - project documentation file
- `pom.xml` - Maven build and dependency configuration
- `testng.xml` - TestNG suite definition
- `run.py` - Flask application entrypoint
- `requirements.txt` - Python dependencies for the Flask app
- `app/` - local Flask application source and templates
- `src/main/java/framework/` - automation framework utilities, driver management, app process control
- `src/test/java/` - Cucumber runner, hooks, and step definitions
- `src/test/resources/` - feature files, test configuration, env properties, and Log4j config
- `target/` - Maven build output, reports, and generated artifacts
- `.claude/` - Claude Code agent/skill/command definitions and security hooks for the SDLC workflow
- `CLAUDE.md` - project-wide instructions Claude Code loads automatically
- `.mcp.json` - MCP server definitions (Jira/Confluence, GitHub, TestRail, Playwright)
- `agent-output/` - generated documentation, requirement artifacts, execution summaries, and automation outputs

## `.claude/` Directory

The `.claude/` folder contains the Claude Code agent tooling that drives the SDLC workflow:

- `.claude/agents/` - subagent definitions for each workflow phase (read-user-story, create-architecture, design-reviewer, implementation-plan, create-implementation, code-reviewer, test-case-creation, automation-test-cases, pull-request-creator)
- `.claude/skills/` - the corresponding skill instructions each subagent invokes
- `.claude/commands/run-sdlc-workflow.md` - the `/run-sdlc-workflow` slash command that orchestrates all phases end-to-end
- `.claude/settings.json` and `.claude/hooks/` - `PreToolUse`/`PostToolUse` security guardrail hooks (sensitive data and malicious input checks, audit logging)
- `.claude/codereview-checklist.md` - the mandatory checklist used by the code-reviewer skill

> Note: This repository includes `.claude/skills/automation-test-cases/SKILL.md` and related definition files used by the automation framework and agent workflows.

## `agent-output/` Directory

The `agent-output/` folder stores generated artifacts and documentation from automated workflows and analysis activities:

- `requirements.md` / `architecture.md` / `design-review.md` / `impl-plan.md` / `create-impl.md` — project planning and analysis artifacts
- `automation_test_results.md` — structured test results or execution summaries
- `code_review.md` / `review-comment.md` — review output from analysis or agent guidance
- `pr_output.md` — generated pull request text and change summaries
- `test_cases.md` — generated or captured test cases for the project

These files are typically produced by the automation/agent workflow rather than manually authored application source.

## Application Details

The Flask app implements a simple authentication flow and automation support endpoints:

- `/login` - login form with username/password validation
- `/forgot-password` - password reset request form
- `/reset-password` - reset password form driven by token validation
- `/dashboard` - protected landing page after successful login
- `/logout` - session termination
- `/__automation__/reset` - automation-only endpoint to reset test state
- `/__automation__/clock/advance` - automation-only endpoint to advance the test clock
- `/__automation__/health` - automation-only health check endpoint

The app seeds a default user in memory:
- Username: `scrum50`
- Password: `Password1!`

## Automation Framework

The automation suite is configured to run Cucumber feature files through TestNG:

- `src/test/java/runner/TestRunner.java` - Cucumber TestNG runner
- `src/test/resources/features/` - BDD scenarios for login, forgot password, and reset password flows
- `src/test/java/stepDefinitions/AuthenticationSteps.java` - step definitions implementing the feature steps
- `src/main/java/framework/drivers/DriverManager.java` - thread-safe WebDriver lifecycle management
- `src/main/java/framework/utils/AppProcessManager.java` - local app process startup, health checks, and reset support
- `src/main/java/framework/utils/config.java` - reusable configuration getters for test execution

### Feature coverage

The existing feature files include:

- `login.feature` - login success, validation, invalid password, account lockout, redirect protection
- `forgot_password.feature` - reset link request behavior for valid/unregistered identifiers
- `reset_password.feature` - reset password form and token validation flows

## Configuration

Primary automation settings are stored in `src/test/resources/config.properties`:

- `baseUrl` - browser automation entrypoint (default: `http://127.0.0.1:5001`)
- `browser` - browser type used by automation
- `headless` - whether browser runs headless
- `timeoutSeconds` - explicit wait timeout
- `pollingSeconds` - element polling interval
- `reportPath` - Extent report output path
- `screenshotPath` - failure screenshot directory
- `notificationLogPath` - local reset notification log file

Environment-specific values are stored in `src/test/resources/env.properties`:

- `qa.baseUrl` - application base URL
- `qa.apiUrl` - API/automation base URL
- `qa.username` / `qa.password` - default credentials
- `qa.resetPassword` - new password value used for reset validation

## Setup and Installation

1. Install Java 17 and Maven.
2. Install Python 3.
3. Create and activate a Python virtual environment (recommended).
4. Install Python dependencies:
   ```bash
   pip install -r requirements.txt
   ```

## Running the Flask Application

Start the local app from the repository root:

```bash
python run.py
```

Default host and port:
- `http://127.0.0.1:5000`

The automation process may override the port based on `src/test/resources/config.properties`.

## Running the Automation Suite

From the repository root, execute:

```bash
mvn test
```

This runs the Cucumber scenarios defined under `src/test/resources/features` using the TestNG suite in `testng.xml`.

## Reports and Artifacts

After execution, review:

- `target/cucumber-report.html`
- `target/cucumber-report.json`
- `target/extent-report.html`
- `target/screenshots/` (captured failures)
- `target/surefire-reports/` (TestNG result XML and HTML)

## Helpful Commands

- Run only failed TestNG scenarios:
  ```bash
  mvn test -Dsurefire.suiteXmlFiles=target/surefire-reports/testng-failed.xml
  ```
- View generated report:
  - `target/extent-report.html`
  - `target/cucumber-report.html`

## Notes and Observations

- The project includes both a working Flask app and an automation harness in the same repository.
- The automation suite is designed to start the local app, validate health, and reset state between runs.
- `repo-clean/` contains a parallel copy of the project layout for cleanup or historical reference.
- The `.readme` file is intentionally lower-case to match the requested filename.

## Useful Files

- `README.md` - this file
- `pom.xml` - Maven automation configuration
- `testng.xml` - TestNG suite definition
- `run.py` - Flask app entrypoint
- `requirements.txt` - Python dependencies
- `src/test/resources/config.properties` - automation config values
- `src/test/resources/env.properties` - environment variables for the suite
- `src/test/resources/features/` - Cucumber scenario definitions
- `target/` - generated reports and artifacts
