# Copilot Instructions: Software Development Lifecycle (SDLC) and Automation Framework Standards

## High-Level Expectations
- Read the user story from JIRA and treat it as the source of truth.
- Extract business rules, acceptance criteria, and business entities from the story.
- Create architecture that includes key components, responsibilities, end-to-end data flow, and integration points.
- Conduct a structured design review of the architecture before implementation.
- Develop an implementation plan that breaks the approved architecture into an ordered task list.
- Implement changes strictly according to the approved implementation plan.
- Perform code review of the implementation using Clean Code principles, test coverage, security, correctness, and DRY.
- Generate test cases from requirements in Gherkin format using Given/When/Then.
- Automate generated test cases using Selenium + Java + Cucumber + TestNG + Maven.
- Generate the full framework structure, including workflow, artifacts, and reports.
- Create a pull request for all modified files used in the framework architecture.

## SDLC Flow
The project must follow a complete high-level SDLC workflow:
1. Requirements intake from JIRA user story.
2. Architecture design based on requirements.
3. Design review and iteration.
4. Implementation planning from the approved architecture.
5. Development/implementation of approved plan.
6. Code review and remediation.
7. Test case creation from requirements.
8. Automation execution and reporting.
9. Pull request creation for framework changes.

## Execution Hooks
- Use `hooks.json` to define execution lifecycle hooks for agents.
- Every agent execution must include a `pretool` section before tool use and a `posttool` section after tool use.
- Pretool hooks should run before any external tool call and validate context or agent inputs.
- Posttool hooks should run after external tool use to capture outputs, perform cleanup, and update the merged execution context.
- Hook definitions must be maintained in `hooks.json` and included as part of the orchestrator execution flow.

## Project Standards
### Requirements and User Story
- Always include business rules, acceptance criteria, and business entities.
- Validate that requirements are complete and unambiguous before design.
- Do not implement without clear business intent and acceptance criteria.

### Architecture and Design
- Architecture must include:
  - Key components and responsibilities.
  - End-to-end data flow.
  - Integration points and dependencies.
  - Non-functional requirements.
- Design review must identify risks, gaps, and improvement actions.
- Only approved architecture moves to implementation.

### Implementation Planning
- Implementation plan must be dependency-ordered.
- Capture blockers, assumptions, and required sequence.
- Break work into actionable tasks at a high level.
- Use the plan to guide development and verification.

### Implementation
- Implement according to the approved plan.
- Follow the framework architecture and folder conventions.
- Keep code modular, maintainable, and testable.
- Avoid shortcuts that violate architecture or design standards.

### Code Review
- Review for Clean Code principles.
- Cover test coverage, security, correctness, and DRY.
- Ensure code follows agreed conventions and architecture.
- Resolve all high- and critical-severity issues before proceeding.

### Test Case Creation
- Generate test cases from requirements only.
- Use Gherkin format: Given / When / Then.
- Cover positive, negative, edge, boundary, and validation scenarios.
- Keep test cases traceable to requirements and acceptance criteria.

### Automation
- Automate tests using Selenium + Java + Cucumber + TestNG + Maven.
- Use the hybrid framework structure and conventions defined in this repository.
- Keep automation stable, reusable, and maintainable.
- Report results via Extent Reports and Maven surefire outputs.

## Framework Expectations
### General
- Follow SOLID design principles.
- Enforce DRY and YAGNI.
- Keep responsibilities separated across layers.
- Prefer explicit waits and stable locators.
- Do not hardcode secrets, credentials, or environment-specific values.

### Project Structure
Required structure unless the repository enforces a different layout:
.
|- pom.xml
|- testng.xml
|- src/
|  |- main/
|  |  |- java/
|  |     |- framework/
|  |     |  |- base/
|  |     |  |- drivers/
|  |     |  |- listeners/
|  |     |  |- pages/
|  |     |  |- reports/
|  |     |  |- utils/
|  |- test/
|     |- java/
|     |  |- hooks/
|     |  |- stepDefinitions/
|     |- resources/
|        |- features/
|        |- config.properties
|        |- env.properties
|- target/
   |- extent-report.html

### Key Component Rules
- `pom.xml`: include dependencies for Selenium, Cucumber, TestNG, Extent Reports, Apache POI, and logging.
- `testng.xml`: define browsers, parallel execution, thread count, and test classes.
- `config.properties`: store static framework settings.
- `env.properties`: store multi-environment values without secrets.
- `DriverManager`/`DriverFactory`: thread-safe driver lifecycle.
- `ExtentManager`: singleton report initialization and flush.
- `BasePage`/page objects: reusable actions only, no assertions.
- `Hooks`: setup, teardown, and report lifecycle management.

## Reporting and Artifact Rules
- Generate Extent report for each execution.
- Preserve Maven surefire outputs.
- Capture screenshots on failure and attach them.
- Write execution artifacts to predictable CI-friendly locations.
- Do not recreate report objects per test.
- When a later rerun supersedes an earlier failed run, update the stored automation and PR artifacts so they reflect the latest execution only.

## Core Technology Stack
- Language: Java (LTS)
- Build Tool: Maven
- UI Automation: Selenium WebDriver
- BDD Layer: Cucumber (TestNG integration)
- Execution Engine: TestNG
- Reporting: Extent Reports (+ surefire reports)
- Data: Excel + feature files + properties

## Folder Responsibilities
- src/main/java/framework/base
	- Base classes for page actions and explicit waits.
- src/main/java/framework/drivers
	- Driver bootstrap, ThreadLocal management, browser selection.
- src/main/java/framework/listeners
	- TestNG listeners for start/end hooks and failure screenshots.
- src/main/java/framework/pages
	- Page Object Model classes with locators and reusable action methods.
- src/main/java/framework/reports
	- Extent report setup and lifecycle management.
- src/main/java/framework/utils
	- Framework utility classes like config and environment resolution logic.
- src/test/java/hooks
	- Cucumber hooks for setup and teardown.
- src/test/java/stepDefinitions
	- All step definition implementations.
- src/test/resources/features
	- All feature files grouped by modules.
- src/test/resources/config.properties
	- Static framework configuration values.
- src/test/resources/env.properties
	- Environment specific values for multi-environment execution (qa, uat, staging, prod-like).
- src/test/resources/testdata.xlsx
	- External data set for test cases with larger data sets.

## Framework Rules

- Keep assertion logic in test or step-definition layer.
- Keep page layer focused on locators, actions, and data retrieval.
- Each Feature file must be written in Gherkin format with Given/When/Then steps.
- Common Steps for all scenarios should be written in background section of feature file.
- For maintaining multiple test data sets, use Examples keyword with Scenario Outline in feature files.
- Always ensure every feature file should be linked to a corresponding step definition file.
- Each feature file should have a unique name and use tags to indicate scope (smoke or regression).
- Every step in a feature file should have a corresponding method in the step definition file.
- All step definition classes must remain under src/test/java/stepDefinitions for both smoke and regression scenarios.
- Hooks must remain under src/test/java/hooks and be reusable across smoke and regression execution.
- Use explicit waits; do not use hard waits.
- Use reusable utilities to enforce DRY.
- Use YAGNI and avoid overengineering.
- Strictly Follow SOLID and appropriate patterns (POM, Factory, Singleton, Strategy, Builder where needed).
- Strictly Follow OOPS principles of Java and best practices for maintainable code.
- Implement Abstract Classes and Interfaces where appropriate to enforce reusability and maintainability.
- Also Implement Tagging in feature files to allow selective execution of test cases.
- Use Playwright MCP server for getting locators and other UI elements for automation.

## Workflows and Outputs
- Orchestrator must load instructions once and share them with sub-agents.
- Sub-agents must not reread instruction files during execution.
- Each phase output must be explicit and traceable.
- The overall flow should reflect the full SDLC from requirements to PR.

## Do's
- Follow the SDLC flow and project standards.
- Keep workflow and architecture explicit.
- Use the orchestrator to unify instructions and results.
- Keep automation stable and maintainable.

## Dont's
- Do not ignore business rules or acceptance criteria.
- Do not bypass architecture review or implementation planning.
- Do not use framework shortcuts that reduce maintainability.
- Do not allow sub-agents to independently reinterpret the instructions.