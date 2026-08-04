# SCRUM-50 Implementation Plan

> Artifact status: plan remains the governing execution order after stabilization follow-up work.
> PR scope note: touched so this artifact appears in PR #4 with the latest full-suite stabilization refresh.

## Plan Scope
Implement a simple localhost login web application using a Python web framework for the backend and Bootstrap for the frontend. The scope is limited to the reviewed authentication and password-recovery behavior: login validation, invalid-credential rejection, account lockout after 2 failed attempts with automatic unlock after 60 seconds, forgot-password request handling, single active reset token per user, 60-second token expiry, strong-password enforcement on reset, password hashing, and redirect to a dashboard after successful login.

The plan intentionally excludes unrelated platform features, admin workflows, and broad observability work because those are not supported by the approved architecture or review decisions.

## Dependency-Ordered Task List
1. Project foundation and configuration.
   - Select the approved Python web framework baseline and create the localhost project scaffold.
   - Add environment/config handling, dependency management, session support, and CSRF protection support if required by the framework.
   - Establish a Bootstrap-based layout, base template, and shared form styling.
   - Dependency: none.

2. Persistence model and security primitives.
   - Define the user record fields needed for login, failed-attempt tracking, lockout timing, and password storage.
   - Define the reset-token record/state needed for token value, active/inactive status, creation time, and expiry time.
   - Implement salted password hashing and verification utilities.
   - Implement high-entropy reset-token generation.
   - Dependency: project foundation and configuration.

3. Authentication and password-policy services.
   - Implement the login validation path for mandatory fields and invalid credentials.
   - Implement failed-attempt counting, lockout after the second failure, and automatic lock release after 60 seconds.
   - Implement forgot-password user lookup by username or email.
   - Implement reset-token issuance with one active token per user and invalidation of prior tokens.
   - Implement reset-token expiry checks and password-strength validation for the reset flow.
   - Implement password update and token invalidation on successful reset.
   - Dependencies: persistence model and security primitives.

4. Request handling and UI flow.
   - Create login, forgot-password, reset-password, and dashboard routes/controllers.
   - Bind form submissions to the authentication service and render validation or success states.
   - Ensure redirects follow the reviewed flow: successful login to dashboard, successful password reset to the expected completion state.
   - Dependency: authentication and password-policy services.

5. Notification abstraction and recovery delivery.
   - Add a notification adapter that sends or simulates delivery of the reset link to the registered email address.
   - Keep the adapter behind an interface so local testing can use a stub or capture implementation.
   - Dependency: reset-token issuance and request handling.

6. Bootstrap UI implementation.
   - Build the login, forgot-password, and reset-password screens with Bootstrap.
   - Display validation errors, lockout messages, token-expiry feedback, and password-policy feedback without exposing unnecessary account details.
   - Provide the dashboard view used after successful login.
   - Dependency: request handling and UI flow.

7. Verification and regression coverage.
   - Add tests for mandatory login validation, invalid credentials, lockout behavior, unlock timing, forgot-password lookup, token lifecycle, password-strength rejection, and successful reset/login flows.
   - Verify security rules for password hashing, token single-use behavior, and generic recovery responses.
   - Dependency: implementation of persistence, services, routes, and UI.

## Recommended Execution Order
1. Scaffold the Python project, framework configuration, and Bootstrap base layout.
2. Implement the user and reset-token data model plus hashing/token utilities.
3. Build the authentication and password-recovery service logic.
4. Wire routes/controllers and connect them to the service layer.
5. Add the notification abstraction for reset-link delivery.
6. Finish the Bootstrap screens and dashboard presentation.
7. Add and run focused tests for the login and recovery scenarios.

## Blockers
No architectural blockers remain for this phase. The only implementation dependency to confirm at execution time is the final Python framework choice if it is not already standardized by the delivery team.