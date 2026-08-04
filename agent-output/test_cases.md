## SCRUM-50 Test Cases

> Artifact status: retained as the current test-case catalog for the passing 14-scenario suite.
> PR scope note: touched so this artifact appears in PR #4 with the latest full-suite stabilization refresh.

This artifact reflects the current runnable Java Cucumber automation scope after removal of helper-dependent reset-token and clock-control scenarios.

### Positive Test Cases

Test Type - Positive
Feature: User Login
Description - Registered user should be redirected to the dashboard after submitting valid username and password.
Scenario - Login with valid credentials
Test Steps:-
Given the user is on the Login page
And the user has a valid username "scrum50"
And the user has a valid password "Password1!"
When the user clicks on the Log in button
Then the user should be redirected to the Dashboard page
Test Data :
username - scrum50
password - Password1!
Priority - High,
Severity - High

Test Type - Positive
Feature: Forgot Password
Description - Registered user should be able to request a password reset by entering a valid username.
Scenario - Request password reset with registered username
Test Steps:-
Given the user is on the Forgot Password page
And the user enters a registered username "scrum50"
When the user clicks on the Send reset link button
Then the user should see the message "If the account exists, a reset link has been sent."
Test Data :
identifier - scrum50
Priority - High,
Severity - High

Test Type - Positive
Feature: Forgot Password
Description - Registered user should be able to request a password reset by entering a valid email address.
Scenario - Request password reset with registered email
Test Steps:-
Given the user is on the Forgot Password page
And the user enters a registered email "scrum50@example.com"
When the user clicks on the Send reset link button
Then the user should see the message "If the account exists, a reset link has been sent."
Test Data :
identifier - scrum50@example.com
Priority - High,
Severity - High

### Negative Test Cases

Test Type - Negative
Feature: User Login
Description - Login should fail when both mandatory fields are blank.
Scenario - Submit login with blank username and blank password
Test Steps:-
Given the user is on the Login page
When the user clicks on the Log in button without entering a username and password
Then the user should remain on the Login page
And the user should see the error message "Username and password are required."
Test Data :
username - blank
password - blank
Priority - High,
Severity - Medium

Test Type - Negative
Feature: User Login
Description - Login should fail when the username is blank even if a password is supplied.
Scenario - Submit login with blank username only
Test Steps:-
Given the user is on the Login page
And the user enters password "Password1!"
When the user clicks on the Log in button without entering a username
Then the user should remain on the Login page
And the user should see the error message "Username and password are required."
Test Data :
username - blank
password - Password1!
Priority - High,
Severity - Medium

Test Type - Negative
Feature: User Login
Description - Login should fail when the password is blank even if a username is supplied.
Scenario - Submit login with blank password only
Test Steps:-
Given the user is on the Login page
And the user enters username "scrum50"
When the user clicks on the Log in button without entering a password
Then the user should remain on the Login page
And the user should see the error message "Username and password are required."
Test Data :
username - scrum50
password - blank
Priority - High,
Severity - Medium

Test Type - Negative
Feature: User Login
Description - Login should fail when the submitted credentials do not match an authorized user.
Scenario - Submit login with invalid password
Test Steps:-
Given the user is on the Login page
And the user enters username "scrum50"
And the user enters password "WrongPass1!"
When the user clicks on the Log in button
Then the user should remain on the Login page
And the user should see the error message "Invalid username or password."
Test Data :
username - scrum50
password - WrongPass1!
Priority - High,
Severity - High

Test Type - Negative
Feature: Account Lockout
Description - Locked account should remain inaccessible immediately after lockout.
Scenario - Login succeeds after automatic account unlock
Test Steps:-
Given the user account "scrum50" is locked after 2 failed login attempts
When the user enters username "scrum50" and password "Password1!"
And the user clicks on the Log in button
Then the user should remain on the Login page
And the user should see the error message "Account is locked. Try again later."
Test Data :
username - scrum50
password - Password1!
Priority - High,
Severity - High

Test Type - Negative
Feature: Forgot Password
Description - Forgot password should not reveal whether an unregistered account exists.
Scenario - Request password reset with unregistered identifier
Test Steps:-
Given the user is on the Forgot Password page
And the user enters an unregistered identifier "unknown@example.com"
When the user clicks on the Send reset link button
Then the user should see the message "If the account exists, a reset link has been sent."
Test Data :
identifier - unknown@example.com
Priority - Medium,
Severity - Medium

Test Type - Negative
Feature: Password Reset
Description - Password reset should fail when the token is missing.
Scenario - Open reset password page without a token
Test Steps:-
Given the user opens the Reset Password page without a token
When the page is fully loaded
Then the user should see the error message "Reset token is required."
And the Reset Password page should remain displayed
Test Data :
token - blank
Priority - High,
Severity - Medium

Test Type - Negative
Feature: Password Reset
Description - Password reset should fail when the token does not exist in the active token store.
Scenario - Open reset password page with invalid token
Test Steps:-
Given the user opens the Reset Password page with token "invalid-token-value"
When the page is fully loaded
Then the user should see the error message "Reset token is invalid."
And the Reset Password page should remain displayed
Test Data :
token - invalid-token-value
Priority - High,
Severity - High

### Edge Test Cases

Test Type - Edge
Feature: Account Lockout
Description - Account should lock exactly on the second consecutive failed login attempt.
Scenario - Trigger lockout on second failed login attempt
Test Steps:-
Given the user is on the Login page
And the user enters username "scrum50"
And the user enters password "WrongPass1!"
When the user clicks on the Log in button for the first failed attempt
And the user clicks on the Log in button for the second failed attempt with the same invalid password
Then the user should see the error message "Account locked after 2 failed attempts."
And the account should be marked as locked
Test Data :
username - scrum50
password - WrongPass1!
failed attempts - 2
Priority - High,
Severity - High

### Boundary Test Cases

Test Type - Boundary
Feature: Password Policy
Description - Password reset should accept a password that exactly meets the minimum supported length and composition boundary.
Scenario - Reset password with exactly 8 valid characters
Execution Mode - Manual or future automation after a UI-supported valid token setup exists
Test Steps:-
Given a valid active reset token exists for user "scrum50"
And the user opens the Reset Password page with that valid token
When the user enters new password "Abcdef1!"
And the user clicks on the Reset password button
Then the user should be redirected to the Login page
And the user should see the message "Password reset successfully. Please log in again."
Test Data :
username - scrum50
token state - valid active token required
new password - Abcdef1!
password length - 8
Priority - Medium,
Severity - High

Test Type - Boundary
Feature: Account Lockout
Description - Account lockout threshold should trigger exactly when the failed attempt count reaches 2.
Scenario - Lockout activates at the exact failed-attempt boundary
Execution Mode - Runnable in current automation scope
Test Steps:-
Given the user is on the Login page
And the user enters username "scrum50"
And the user enters password "WrongPass1!"
When the user clicks on the Log in button for the first failed attempt
And the user clicks on the Log in button for the second failed attempt with the same invalid password
Then the user should see the error message "Account locked after 2 failed attempts."
And the account should be marked as locked
Test Data :
username - scrum50
password - WrongPass1!
lockout threshold - 2 failed attempts
Priority - High,
Severity - High

### API Validation Test Cases

Test Type - API Validation
Feature: Reset Password Route
Description - The reset password endpoint should return the required-token validation message when the token query parameter is missing.
Scenario - GET reset password route without token
Execution Mode - Manual HTTP validation or future API automation
Test Steps:-
Given the application is running
When a GET request is sent to "/reset-password" without a token query parameter
Then the response should render the reset password page
And the response content should include the message "Reset token is required."
Test Data :
method - GET
route - /reset-password
token - omitted
Priority - High,
Severity - Medium

Test Type - API Validation
Feature: Forgot Password Route
Description - The forgot password endpoint should return an account-enumeration-safe response for unknown identifiers.
Scenario - POST forgot password route with unknown identifier
Execution Mode - Manual HTTP validation or future API automation
Test Steps:-
Given the application is running
When a POST request is sent to "/forgot-password" with identifier "unknown@example.com"
Then the response should render the forgot password page
And the response content should include the message "If the account exists, a reset link has been sent."
Test Data :
method - POST
route - /forgot-password
identifier - unknown@example.com
Priority - Medium,
Severity - Medium

### UI Validation Test Cases

Test Type - UI Validation
Feature: Forgot Password
Description - Forgot password page should display the expected input, action button, and navigation link.
Scenario - Verify forgot password page controls and labels
Test Steps:-
Given the user opens the Forgot Password page
When the page is fully loaded
Then the forgot password page should display
  | Username or email input field |
  | Send reset link button |
  | Back to login link |
Test Data :
page - forgot password
Priority - Medium,
Severity - Low

### Security / Access Control Test Cases

Test Type - Security
Feature: User Login
Description - Dashboard should not be accessible without an authenticated session.
Scenario - Open dashboard without authenticated session
Test Steps:-
Given the user does not have an authenticated session
When the user opens the Dashboard page directly
Then the user should be redirected to the Login page
Test Data :
session - unauthenticated
target page - /dashboard
Priority - High,
Severity - High

### Runnable Scope Summary

- Runnable feature scenarios documented: 14
- Supplemental non-runnable artifact coverage: 4 cases in Boundary/API sections
- Removed from runnable scope: reset-token generation verification, expired-token coverage, valid-token password reset flows, second-token invalidation coverage, reset-password UI validation requiring a real token, and time-based auto-unlock verification.
