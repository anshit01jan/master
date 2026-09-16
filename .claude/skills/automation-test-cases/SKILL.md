---
name: automation-test-cases
description: "Use when automating all test cases from agent-output/test_cases.md into a Java + Cucumber + Selenium + Maven + TestNG hybrid framework, ensuring complete step-definition coverage for smoke and regression tagged scenarios with hooks under src/test/java, executing via testng.xml, and publishing automation_test_results.md with pass/fail/skip metrics and Extent report URL."
---

# Automation Test Cases Skill

You are a specialist QA automation agent.

Your role is to read test cases from agent-output/test_cases.md and fully automate them using the architecture, rules, and constraints defined in CLAUDE.md.

## Primary Objective

Create or update a production-ready Java + Cucumber + Selenium + Maven + TestNG hybrid automation framework with a data-driven approach, then execute all test cases and publish consolidated execution results. If framework already exists from previous runs, update it to accommodate new test cases and ensure all existing test cases are still functional.

## Mandatory Key Components

### pom.xml
- Must include all dependencies and plugins needed to compile and execute tests via Maven.
- Required dependencies should include at minimum:
	- Selenium Java
	- Cucumber Java
	- Cucumber TestNG
	- TestNG
	- Apache POI (for Excel)
	- Extent Reports
	- Logging dependency (log4j.xml required)
- Required plugins should include at minimum:
	- maven-compiler-plugin
	- maven-surefire-plugin (for TestNG execution)

### src/main/java/framework/utils/config.java
- Must load all properties from src/test/resources/config.properties.
- Provide static getter methods for commonly used keys (url, timeout, browser, etc.).

### src/main/java/framework/utils/environment/env.java
- Must contain environment selection and property loading logic for multi-environment execution.
- Must read active environment from system property (for example `-Denv=qa`) and map values from src/test/resources/env.properties.
- Must expose reusable getters for environment specific values such as baseUrl, apiUrl, and credentials keys.
- Must be reusable across DriverFactory, hooks, and step definitions without duplicated environment parsing logic.

### src/main/java/framework/drivers/DriverManager.java
- Must implement ThreadLocal WebDriver reference with singleton pattern for thread safety.
- Must expose:
	- getDriver()
	- quitDriver()
- Must initialize and manage drivers for parallel and multi browser execution.

### src/main/java/framework/drivers/DriverFactory.java
- Must return driver instance based on browser parameter passed from testng.xml.
- Must use DriverManager.getDriver() to acquire active driver.

### src/main/java/framework/listeners/Listeners.java
- Must implement ITestListener.
- Must log test start, success, failure, and finish events.
- Must capture screenshots on test failure and attach/report them.

### src/main/java/framework/utils/Logger.java
- Must provide static logging methods for info, debug, warn, and error levels.

### src/main/java/framework/base/BasePage.java
- Must include constructor accepting WebDriver and WebDriverWait references.
- Must include reusable methods such as:
	- waitForElementToBePresent
	- clickOnElement
	- selectElementFromDropdown
	- enterTextInInputBox
	- plus other common action/utility wrappers needed for framework reuse.

### src/main/java/framework/pages
- Must contain page classes for each web page/module.
- Each page class should:
	- Extend or use BasePage functionality
	- Keep locators and page specific action methods
	- Build reusable generic methods by composing BasePage methods
	- Avoid assertion logic in page layer

### src/main/java/framework/reports/ExtentManager.java
- Must manage singleton ExtentReports instance.
- Must initialize report once per run and flush once at run completion.

### src/test/java/stepDefinitions
- Must include all relevant step definition classes.
- Keep assertions in this layer (or immediate test layer wrappers), not in pages.

### src/test/java/hooks/Hooks.java
- Must handle test setup and teardown.
- Must call DriverFactory for driver initialization.
- Must call ExtentManager for reporting lifecycle.
- Must not reinitialize Extent report for each individual test.

### src/test/resources/features
- Must include module wise feature files.
- Feature files should use:
	- Scenario
	- Scenario Outline
	- Data Tables
	- Examples
- Tags are mandatory for all scenarios.

### src/test/resources/config.properties
- Must include static and environment values such as:
	- baseUrl
	- browser
	- timeout values
	- wait/polling values
	- report paths

### src/test/resources/env.properties
- Must include environment specific configuration blocks/keys for each target environment.
- Should include values for at least:
	- env specific baseUrl
	- env specific apiUrl (if applicable)
	- env specific non-sensitive toggles/timeouts
- Sensitive values must not be stored directly in this file.

### src/test/resources/log4j.xml
- Must define logging levels and output formats.

### TestRunner
- Must use Cucumber TestNG runner annotations to execute feature files.
- Must support TestNG parallel execution.
- Typical implementation should extend AbstractTestNGCucumberTests and override scenarios() with a DataProvider(parallel = true) when parallelization is required.

### testng.xml
- Must define:
	- browser parameter(s)
	- classes/methods to run
	- listeners if needed

## Required Workflow

Follow these steps in order and do not skip any:

1. Read agent-output/test_cases.md completely.
2. Extract complete details for each test case:
	 - Test case name
	 - Description
	 - Test steps
	 - Test data
	 - Expected result
	 - Priority and severity
3. Implement or update framework components as needed using Java + Cucumber + Selenium + Maven + TestNG with Page Object Model and data-driven design.
4. Add assertions for all necessary validation points in each automated test case.
5. Ensure all High priority and non-High priority automated scenarios have corresponding step definitions under src/test/java/stepDefinitions.
6. Ensure hooks are implemented and maintained under src/test/java/hooks.
7. Ensure implementation strictly follows CLAUDE.md for:
	 - src/main/java/framework (base, drivers, listeners, pages, reports, utils)
	 - src/main/java/framework/utils/environment/env.java
	 - src/test/resources/config.properties and src/test/resources/env.properties
	 - ThreadLocal WebDriver lifecycle, Cucumber + TestNG runner, reporting, and data rules
8. Run all tests using testng.xml.
9. Collect execution outcomes from generated reports and logs.
10. Create target folder and generate Extent reports. Map all the test cases to their corresponding Extent report entries. All the test cases steps and expected results must be reflected in the Extent report.
11. Update agent-output/automation_test_results.md in tabular format with:
		- total count
		- passed count and percentage
		- failed count and percentage
		- skipped count and percentage
		- overall pass percentage
12. Validate that the final report reflects the actual outcome of the latest run only. Never carry forward a previous passing result to mask a real failure in the current run.
13. Always use the Playwright MCP tool to navigate to base url and other web pages and capture all the necessary locators required for each web page and store them in the appropriate page object class.

## Classification Rules

- High priority test cases must be tagged and executable in smoke scope.
- Medium and Low priority test cases must be tagged and executable in regression scope.
- Scope separation must be achieved via feature tags and runner/TestNG filtering, not via src/test/java/smoke or src/test/java/regression folders.

## Data and Environment Rules

- Use feature files for small datasets and readable scenario examples.
- Use src/test/resources/config.properties for static defaults.
- Use src/test/resources/env.properties for multi-environment values.
- Use src/main/java/framework/utils/environment/env.java as the single environment resolver.

## Execution and Reporting Rules

- Execute through Maven/TestNG with testng.xml configuration.
- Ensure parallel execution compatibility with ThreadLocal driver management.
- Generate Extent and surefire/failsafe compatible outputs.
- Include screenshot evidence paths for failed tests where available.
- Do not publish a partial report.

## Failure Handling

- If execution is blocked by environment/app unavailability, still generate automation_test_results.md with:
	- blocker summary
	- impacted test cases
	- environment root cause
	- actionable rerun steps

## Completion Criteria

Task is complete only when all are true:

1. All test cases from agent-output/test_cases.md are mapped and automated.
2. Smoke and regression classification follows priority rules via tags/filters, without creating src/test/java/smoke or src/test/java/regression folders.
3. testng.xml execution has been attempted and outcomes captured.
4. agent-output/automation_test_results.md is updated with full counts, percentages, and Extent report URL/path, reflecting the true outcome of the latest run.
5. Implementation aligns with CLAUDE.md.
