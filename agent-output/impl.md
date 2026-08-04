# SCRUM-50 Implementation Specification

> Artifact status: implementation specification remains in scope for the final validated solution.
> PR scope note: touched so this artifact appears in PR #4 with the latest full-suite stabilization refresh.

## Technology Stack
- Backend: Flask
- Frontend: Bootstrap
- Templates: Jinja2
- Runtime: Python on localhost
- Persistence: In-memory application store for localhost execution and deterministic workflow validation

## Scope
Implement a simple localhost login web application for SCRUM-50 with these features:
- Login with username and password
- Mandatory field validation for login
- Invalid credential rejection
- Account lockout after 2 failed attempts
- Automatic account unlock after 60 seconds
- Forgot-password initiation using registered username or email
- Reset-link generation and delivery through a notification abstraction
- Only one active reset token per user at a time
- Reset-token expiry after 60 seconds
- Password reset enforcement for minimum length 8 with at least one uppercase letter, one lowercase letter, one digit, and one special character
- Invalidate previous reset tokens after successful password reset
- Redirect successful login to the dashboard

## Implementation Constraints
- Follow the approved architecture and design-review decisions.
- Store passwords as salted one-way hashes.
- Generate high-entropy single-use reset tokens.
- Keep authentication, lockout, and token state centralized and testable.
- Do not implement unrelated platform or admin features.

## Execution Order
1. Scaffold the Flask application and Bootstrap base layout.
2. Implement the in-memory user and reset-token store plus hashing and token utilities.
3. Implement authentication, lockout, forgot-password, reset-token, and password-policy services.
4. Add routes for login, forgot-password, reset-password, logout, and dashboard.
5. Add notification stubbing for reset-link delivery.
6. Complete Bootstrap screens and feedback states.
7. Add focused automated tests for the core flows.