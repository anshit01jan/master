# SCRUM-50 Requirements

> Artifact status: requirements baseline retained for the final passing workflow output.
> PR scope note: touched so this artifact appears in PR #4 with the latest full-suite stabilization refresh.

## Description
Registered users must be able to log in securely and recover their password through a controlled reset flow.

The login flow must accept a username and password, validate mandatory fields, authenticate against the authorized user repository, deny invalid credentials, lock the account after 2 failed attempts, keep locked accounts inaccessible until it unlocks automatically after 60 seconds, and redirect successful login to the dashboard.

The forgot password flow must accept a registered username or email, send a reset link to the registered email, allow only one active reset token per user, enforce a reset-token expiry of 60 seconds, require a strong password during reset with a minimum length of 8 characters and at least one uppercase letter, one lowercase letter, one digit, and one special character, and invalidate previous reset tokens after a successful reset.

The story references business rules BR-001 to BR-009 and acceptance criteria AC-001 to AC-012, but those items are not expanded in the Jira text provided.

## Functional Requirements
- The system shall accept username and password for login.
- The system shall validate mandatory login fields.
- The system shall authenticate credentials against the authorized user repository.
- The system shall deny login for invalid credentials.
- The system shall lock an account after 2 failed login attempts.
- The system shall keep locked accounts inaccessible until they unlock automatically after 60 seconds.
- The system shall redirect a successful login to the dashboard.
- The system shall accept a registered username or email for password recovery.
- The system shall send a password reset link to the registered email address.
- The system shall allow only one active reset token per user at a time.
- The system shall enforce a reset-token expiry of 60 seconds.
- The system shall require a strong password during password reset with a minimum length of 8 characters and at least one uppercase letter, one lowercase letter, one digit, and one special character.
- The system shall invalidate previous reset tokens after a successful password reset.

## Non-Functional Requirements
- Secure handling of authentication and password reset flows is implied by the story.
- No other explicit non-functional requirements are stated in the Jira text.

## Acceptance Criteria
- Successful login redirects the user to the dashboard.
- Invalid credentials are denied.
- Mandatory login fields are validated.
- Forgot password can be initiated using a registered username or email.
- A reset link is sent to the registered email address.
- Only one active reset token exists per user.
- Reset tokens expire after 60 seconds.
- Password reset requires a minimum password length of 8 characters and at least one uppercase letter, one lowercase letter, one digit, and one special character.
- Previous reset tokens are invalidated after a successful reset.
- Account lockout occurs after 2 failed login attempts.
- Locked accounts remain inaccessible until they unlock automatically after 60 seconds.
- Login and reset validations are testable.

## Business Rules
- Login requires a username and password.
- Login must fail when credentials are invalid.
- Accounts must lock after 2 failed login attempts.
- Locked accounts must stay inaccessible until they unlock automatically after 60 seconds.
- Forgot password must use a registered username or email.
- Reset links must be sent to the registered email address only.
- Each user may have only one active reset token at a time.
- Reset tokens must expire after 60 seconds.
- Password reset must enforce a minimum password length of 8 characters and at least one uppercase letter, one lowercase letter, one digit, and one special character.
- Previous reset tokens must be invalidated after a successful password reset.
- The story references BR-001 to BR-009, but the Jira text does not define the individual rule statements.

## Ambiguities
- The detailed statements for BR-001 to BR-009 and AC-001 to AC-012 are not included in the Jira text.