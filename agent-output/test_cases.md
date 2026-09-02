# Test Cases - User Authentication System (SCRUM-31)

## Login Functionality Tests

### TC-001: Positive - Valid Login
**Test Type:** Positive  
**Feature:** User Login  
**Description:** User should be authenticated with valid username/email and password and redirected to dashboard  
**Scenario:** Login with valid credentials  
**Test Steps:**
- Given the user is on the Login page
- And the user has entered a valid email "testuser@example.com"
- And the user has entered a valid password "Password@123"
- When the user clicks the Login button
- Then the user should be redirected to the Dashboard page
- And a session should be created for the user

**Test Data:**
- Email: testuser@example.com
- Password: Password@123

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** AC-001, FR-4

---

### TC-002: Negative - Invalid Password
**Test Type:** Negative  
**Feature:** User Login  
**Description:** User login should fail with invalid password and display error message  
**Scenario:** Login with invalid password for valid user  
**Test Steps:**
- Given the user is on the Login page
- And the user has entered a valid email "testuser@example.com"
- And the user has entered an invalid password "WrongPassword@123"
- When the user clicks the Login button
- Then the login should fail
- And an error message "Invalid credentials" should be displayed
- And the user should remain on the Login page

**Test Data:**
- Email: testuser@example.com
- Password: WrongPassword@123

**Priority:** High  
**Severity:** High  
**Requirement Link:** AC-002, FR-5

---

### TC-003: Negative - Non-Existent User
**Test Type:** Negative  
**Feature:** User Login  
**Description:** Login should fail for non-registered user email  
**Scenario:** Login with non-existent email  
**Test Steps:**
- Given the user is on the Login page
- And the user has entered a non-existent email "nonexistent@example.com"
- And the user has entered any password "Password@123"
- When the user clicks the Login button
- Then the login should fail
- And an error message "Invalid credentials" should be displayed (without revealing user doesn't exist)
- And the user should remain on the Login page

**Test Data:**
- Email: nonexistent@example.com
- Password: Password@123

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-5, NFR-6

---

### TC-004: Negative - Empty Email Field
**Test Type:** UI Validation  
**Feature:** User Login  
**Description:** Login form should not submit when email field is empty  
**Scenario:** Login with empty email field  
**Test Steps:**
- Given the user is on the Login page
- And the email field is left empty
- And the user has entered a password "Password@123"
- When the user clicks the Login button
- Then the form should not submit
- And a validation message should be displayed for the email field

**Test Data:**
- Email: (empty)
- Password: Password@123

**Priority:** High  
**Severity:** High  
**Requirement Link:** AC-003, FR-2, FR-6

---

### TC-005: Negative - Empty Password Field
**Test Type:** UI Validation  
**Feature:** User Login  
**Description:** Login form should not submit when password field is empty  
**Scenario:** Login with empty password field  
**Test Steps:**
- Given the user is on the Login page
- And the user has entered a valid email "testuser@example.com"
- And the password field is left empty
- When the user clicks the Login button
- Then the form should not submit
- And a validation message should be displayed for the password field

**Test Data:**
- Email: testuser@example.com
- Password: (empty)

**Priority:** High  
**Severity:** High  
**Requirement Link:** AC-003, FR-2, FR-6

---

### TC-006: Negative - Both Fields Empty
**Test Type:** UI Validation  
**Feature:** User Login  
**Description:** Login form should not submit when both email and password fields are empty  
**Scenario:** Login with both fields empty  
**Test Steps:**
- Given the user is on the Login page
- And the email field is left empty
- And the password field is left empty
- When the user clicks the Login button
- Then the form should not submit
- And validation messages should be displayed for both fields

**Test Data:**
- Email: (empty)
- Password: (empty)

**Priority:** High  
**Severity:** High  
**Requirement Link:** AC-003, FR-2, FR-6

---

### TC-007: Negative - SQL Injection in Email Field
**Test Type:** Security/Negative  
**Feature:** User Login  
**Description:** System should safely handle SQL injection attempts in email field  
**Scenario:** Login with SQL injection payload in email  
**Test Steps:**
- Given the user is on the Login page
- And the user has entered a SQL injection payload "' OR '1'='1" in email field
- And the user has entered any password
- When the user clicks the Login button
- Then the login should fail safely
- And an error message "Invalid credentials" should be displayed
- And no SQL error should be exposed to the user

**Test Data:**
- Email: ' OR '1'='1
- Password: Password@123

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** NFR-6

---

### TC-008: Edge - Email with Whitespace
**Test Type:** Edge  
**Feature:** User Login  
**Description:** System should trim whitespace from email input  
**Scenario:** Login with email containing leading/trailing whitespace  
**Test Steps:**
- Given the user is on the Login page
- And the user has entered " testuser@example.com " (with spaces)
- And the user has entered a valid password "Password@123"
- When the user clicks the Login button
- Then the system should trim whitespace and authenticate the user
- And the user should be redirected to the Dashboard page

**Test Data:**
- Email: " testuser@example.com "
- Password: Password@123

**Priority:** Medium  
**Severity:** Medium  
**Requirement Link:** FR-3

---

### TC-009: Edge - Email Case Insensitivity
**Test Type:** Edge  
**Feature:** User Login  
**Description:** Login should be case-insensitive for email addresses  
**Scenario:** Login with uppercase email  
**Test Steps:**
- Given the user is on the Login page
- And the user has registered with email "testuser@example.com"
- And the user has entered email "TESTUSER@EXAMPLE.COM" (uppercase)
- And the user has entered a valid password "Password@123"
- When the user clicks the Login button
- Then the system should authenticate the user (case-insensitive)
- And the user should be redirected to the Dashboard page

**Test Data:**
- Registered Email: testuser@example.com
- Login Email: TESTUSER@EXAMPLE.COM
- Password: Password@123

**Priority:** Medium  
**Severity:** Medium  
**Requirement Link:** FR-3

---

## Account Lockout Tests

### TC-010: Negative - First Failed Attempt
**Test Type:** Negative  
**Feature:** Account Lockout  
**Description:** System should track first failed login attempt  
**Scenario:** First failed login attempt  
**Test Steps:**
- Given the user is on the Login page
- And the user has entered a valid email "testuser@example.com"
- And the user has entered an invalid password "WrongPassword@123"
- When the user clicks the Login button
- Then the login should fail
- And an error message should be displayed
- And the failed attempt counter should be incremented to 1
- And the user account should NOT be locked

**Test Data:**
- Email: testuser@example.com
- Password: WrongPassword@123

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-7

---

### TC-011: Negative - Second Failed Attempt
**Test Type:** Negative  
**Feature:** Account Lockout  
**Description:** System should track second failed login attempt  
**Scenario:** Second consecutive failed login attempt  
**Test Steps:**
- Given the user has already failed 1 login attempt
- And the user is on the Login page again
- And the user has entered a valid email "testuser@example.com"
- And the user has entered an invalid password "WrongPassword@123"
- When the user clicks the Login button
- Then the login should fail
- And the failed attempt counter should be incremented to 2
- And the user account should NOT be locked

**Test Data:**
- Email: testuser@example.com
- Password: WrongPassword@123

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-7

---

### TC-012: Negative - Third Failed Attempt Locks Account
**Test Type:** Negative  
**Feature:** Account Lockout  
**Description:** System should lock account after 3 failed login attempts  
**Scenario:** Third consecutive failed login attempt triggers account lock  
**Test Steps:**
- Given the user has already failed 2 login attempts
- And the user is on the Login page again
- And the user has entered a valid email "testuser@example.com"
- And the user has entered an invalid password "WrongPassword@123"
- When the user clicks the Login button
- Then the login should fail
- And the failed attempt counter should be incremented to 3
- And the user account should be locked
- And an error message "Your account has been locked due to multiple failed login attempts" should be displayed
- And the account lockout timestamp should be recorded

**Test Data:**
- Email: testuser@example.com
- Password: WrongPassword@123

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** AC-005, FR-8, FR-9

---

### TC-013: Negative - Locked Account Access Denied
**Test Type:** Negative  
**Feature:** Account Lockout  
**Description:** Locked account should be denied access even with correct password  
**Scenario:** Login attempt with correct credentials while account is locked  
**Test Steps:**
- Given the user's account is locked after 3 failed attempts
- And the user is on the Login page
- And the user has entered a valid email "testuser@example.com"
- And the user has entered the correct password "Password@123"
- When the user clicks the Login button
- Then the login should fail
- And an error message "Your account is locked" should be displayed
- And the user should not be authenticated
- And the user should remain on the Login page

**Test Data:**
- Email: testuser@example.com
- Password: Password@123 (correct password)

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** AC-005, FR-9

---

### TC-014: Positive - Account Auto-Unlock After 1 Minute
**Test Type:** Positive  
**Feature:** Account Lockout  
**Description:** Account should automatically unlock after 1 minute of lockout  
**Scenario:** Locked account unlocks after 1 minute expiration  
**Test Steps:**
- Given the user's account is locked after 3 failed attempts
- And exactly 1 minute has passed since the account was locked
- And the user is on the Login page
- And the user has entered the valid email "testuser@example.com"
- And the user has entered the correct password "Password@123"
- When the user clicks the Login button
- Then the account should be unlocked
- And the user should be authenticated
- And the user should be redirected to the Dashboard page
- And the failed attempt counter should be reset to 0

**Test Data:**
- Email: testuser@example.com
- Password: Password@123
- Lockout duration: 1 minute

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-10

---

### TC-015: Negative - Account Locked Before 1 Minute Elapses
**Test Type:** Negative  
**Feature:** Account Lockout  
**Description:** Locked account should remain locked if less than 1 minute has passed  
**Scenario:** Login attempt before lockout duration expires  
**Test Steps:**
- Given the user's account is locked after 3 failed attempts
- And only 30 seconds have passed since the account was locked
- And the user is on the Login page
- And the user has entered a valid email "testuser@example.com"
- And the user has entered the correct password "Password@123"
- When the user clicks the Login button
- Then the login should fail
- And an error message "Your account is locked" should be displayed
- And the account should remain locked

**Test Data:**
- Email: testuser@example.com
- Password: Password@123
- Elapsed time: 30 seconds

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-10

---

### TC-016: Positive - Failed Attempt Counter Reset After Successful Login
**Test Type:** Positive  
**Feature:** Account Lockout  
**Description:** Failed attempt counter should reset after successful login  
**Scenario:** Successful login resets failed attempt counter  
**Test Steps:**
- Given the user has failed 1 login attempt previously
- And the user is on the Login page
- And the user has entered a valid email "testuser@example.com"
- And the user has entered the correct password "Password@123"
- When the user clicks the Login button
- Then the user should be authenticated
- And the user should be redirected to the Dashboard page
- And the failed attempt counter should be reset to 0

**Test Data:**
- Email: testuser@example.com
- Password: Password@123

**Priority:** Medium  
**Severity:** Medium  
**Requirement Link:** FR-7

---

## Forgot Password Functionality Tests

### TC-017: Positive - Forgot Password Link Visible on Login Page
**Test Type:** Positive  
**Feature:** Forgot Password  
**Description:** Forgot Password link should be visible and accessible on Login page  
**Scenario:** Verify Forgot Password option is available on Login page  
**Test Steps:**
- Given the user is on the Login page
- Then a "Forgot Password?" link should be visible on the page
- And the link should be clickable

**Test Data:** N/A

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-11

---

### TC-018: Positive - Navigate to Forgot Password Form
**Test Type:** Positive  
**Feature:** Forgot Password  
**Description:** Clicking Forgot Password link should navigate to the password reset form  
**Scenario:** User navigates to Forgot Password form  
**Test Steps:**
- Given the user is on the Login page
- When the user clicks the "Forgot Password?" link
- Then the user should be redirected to the Forgot Password page
- And a form to enter the email address should be displayed

**Test Data:** N/A

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-11

---

### TC-019: Positive - Valid Email Reset Request
**Test Type:** Positive  
**Feature:** Forgot Password  
**Description:** System should send password reset link for valid registered email  
**Scenario:** Password reset email sent for valid email address  
**Test Steps:**
- Given the user is on the Forgot Password page
- And the user has entered a valid registered email "testuser@example.com"
- When the user clicks the "Send Reset Link" button
- Then a success message "Password reset link has been sent to your email" should be displayed
- And a password reset email should be sent to the registered email address
- And the email should contain a valid password reset link
- And the password reset token should be stored with expiration time of 2 minutes

**Test Data:**
- Email: testuser@example.com

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** AC-004, FR-12, FR-13

---

### TC-020: Negative - Non-Registered Email Reset Request
**Test Type:** Negative  
**Feature:** Forgot Password  
**Description:** System should handle non-registered email gracefully without revealing user doesn't exist  
**Scenario:** Password reset request with non-registered email  
**Test Steps:**
- Given the user is on the Forgot Password page
- And the user has entered a non-registered email "nonexistent@example.com"
- When the user clicks the "Send Reset Link" button
- Then a success message should be displayed (for security, not revealing whether email exists)
- And no email should be sent
- And the user should not be able to track that the email doesn't exist in the system

**Test Data:**
- Email: nonexistent@example.com

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-12, NFR-6

---

### TC-021: Negative - Empty Email in Forgot Password Form
**Test Type:** UI Validation  
**Feature:** Forgot Password  
**Description:** Forgot Password form should not submit with empty email field  
**Scenario:** Forgot Password with empty email field  
**Test Steps:**
- Given the user is on the Forgot Password page
- And the email field is left empty
- When the user clicks the "Send Reset Link" button
- Then the form should not submit
- And a validation message should be displayed for the email field

**Test Data:**
- Email: (empty)

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-12

---

### TC-022: Negative - Invalid Email Format in Forgot Password Form
**Test Type:** Negative  
**Feature:** Forgot Password  
**Description:** System should reject invalid email format  
**Scenario:** Forgot Password with invalid email format  
**Test Steps:**
- Given the user is on the Forgot Password page
- And the user has entered an invalid email format "notanemail"
- When the user clicks the "Send Reset Link" button
- Then the form should not submit
- And a validation message "Please enter a valid email address" should be displayed

**Test Data:**
- Email: notanemail

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-12

---

### TC-023: Positive - Click Reset Link in Email
**Test Type:** Positive  
**Feature:** Forgot Password  
**Description:** User should be able to click reset link from email and navigate to reset page  
**Scenario:** Navigate to password reset page via email link  
**Test Steps:**
- Given the user has received a password reset email
- And the email contains a password reset link with a valid token
- When the user clicks the password reset link in the email
- Then the user should be redirected to the password reset page
- And a form to enter a new password should be displayed
- And the reset token should be validated

**Test Data:**
- Reset Token: Valid, unexpired token

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** FR-13, FR-15

---

### TC-024: Negative - Expired Reset Link
**Test Type:** Negative  
**Feature:** Forgot Password  
**Description:** System should reject expired password reset links  
**Scenario:** Password reset with expired token (after 2 minutes)  
**Test Steps:**
- Given the user has received a password reset email
- And exactly 2 minutes and 1 second have passed since the reset link was generated
- When the user tries to click the expired password reset link
- Then the user should receive an error message "Reset link has expired. Please request a new password reset."
- And the user should be redirected to the Forgot Password page
- And the reset should not be allowed

**Test Data:**
- Reset Token: Valid token, but expired (>2 minutes)

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-15

---

### TC-025: Negative - Invalid/Tampered Reset Token
**Test Type:** Security/Negative  
**Feature:** Forgot Password  
**Description:** System should reject invalid or tampered reset tokens  
**Scenario:** Password reset with tampered token  
**Test Steps:**
- Given the user is trying to access the password reset page
- When the user provides an invalid or tampered token in the URL
- Then the system should display an error message "Invalid or expired reset link"
- And the password reset should not be allowed
- And the user should be redirected to the Forgot Password page

**Test Data:**
- Reset Token: Invalid/tampered token

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** FR-13

---

### TC-026: Negative - Single Active Token (New Request Invalidates Previous)
**Test Type:** Negative  
**Feature:** Forgot Password  
**Description:** New password reset request should invalidate previous token  
**Scenario:** Multiple password reset requests - only latest token is valid  
**Test Steps:**
- Given the user has received a password reset email with Token A
- And the user requests another password reset (receives Token B)
- When the user tries to use Token A to reset password
- Then the system should display an error message "This reset link is no longer valid"
- And the password reset should not be allowed
- And only Token B should be valid

**Test Data:**
- Token A: First reset token (invalidated)
- Token B: Second reset token (active)

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-14

---

### TC-027: Positive - Strong Password with Reset
**Test Type:** Positive  
**Feature:** Forgot Password  
**Description:** User should be able to set a strong password during reset  
**Scenario:** Password reset with strong password  
**Test Steps:**
- Given the user is on the password reset page with a valid, unexpired token
- And the user has entered a strong password "NewPassword@123"
- When the user clicks the "Reset Password" button
- Then the password reset should succeed
- And a success message "Your password has been successfully reset" should be displayed
- And the user should be redirected to the Login page
- And the user should be able to login with the new password

**Test Data:**
- New Password: NewPassword@123 (meets strong password criteria)

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** FR-16, AC-004

---

### TC-028: Negative - Weak Password with Reset
**Test Type:** Negative  
**Feature:** Forgot Password  
**Description:** System should reject weak passwords during reset  
**Scenario:** Password reset with weak password  
**Test Steps:**
- Given the user is on the password reset page with a valid, unexpired token
- And the user has entered a weak password "123456"
- When the user clicks the "Reset Password" button
- Then the password reset should fail
- And an error message should be displayed describing password requirements
- And a message containing "Password must contain at least 8 characters with 1 uppercase, 1 lowercase, and 1 special character" should be shown
- And the user should remain on the password reset page

**Test Data:**
- New Password: 123456 (weak - too short, no uppercase/lowercase/special chars)

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-16, FR-17

---

### TC-029: Negative - Password Missing Uppercase
**Test Type:** Negative  
**Feature:** Forgot Password  
**Description:** System should require uppercase letter in password  
**Scenario:** Password reset with password missing uppercase letter  
**Test Steps:**
- Given the user is on the password reset page with a valid, unexpired token
- And the user has entered a password "newpassword@123" (no uppercase)
- When the user clicks the "Reset Password" button
- Then the password reset should fail
- And an error message should be displayed indicating uppercase letter is required

**Test Data:**
- New Password: newpassword@123 (missing uppercase)

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-17

---

### TC-030: Negative - Password Missing Lowercase
**Test Type:** Negative  
**Feature:** Forgot Password  
**Description:** System should require lowercase letter in password  
**Scenario:** Password reset with password missing lowercase letter  
**Test Steps:**
- Given the user is on the password reset page with a valid, unexpired token
- And the user has entered a password "NEWPASSWORD@123" (no lowercase)
- When the user clicks the "Reset Password" button
- Then the password reset should fail
- And an error message should be displayed indicating lowercase letter is required

**Test Data:**
- New Password: NEWPASSWORD@123 (missing lowercase)

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-17

---

### TC-031: Negative - Password Missing Special Character
**Test Type:** Negative  
**Feature:** Forgot Password  
**Description:** System should require special character in password  
**Scenario:** Password reset with password missing special character  
**Test Steps:**
- Given the user is on the password reset page with a valid, unexpired token
- And the user has entered a password "NewPassword123" (no special character)
- When the user clicks the "Reset Password" button
- Then the password reset should fail
- And an error message should be displayed indicating special character is required

**Test Data:**
- New Password: NewPassword123 (missing special character)

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-17

---

### TC-032: Negative - Password Less Than 8 Characters
**Test Type:** Negative  
**Feature:** Forgot Password  
**Description:** System should require minimum 8 characters in password  
**Scenario:** Password reset with password less than 8 characters  
**Test Steps:**
- Given the user is on the password reset page with a valid, unexpired token
- And the user has entered a password "New@12" (7 characters)
- When the user clicks the "Reset Password" button
- Then the password reset should fail
- And an error message should be displayed indicating minimum 8 characters required

**Test Data:**
- New Password: New@12 (7 characters, less than minimum)

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-17

---

### TC-033: Negative - Empty Password Field in Reset
**Test Type:** UI Validation  
**Feature:** Forgot Password  
**Description:** Password reset form should not submit with empty password field  
**Scenario:** Password reset with empty password field  
**Test Steps:**
- Given the user is on the password reset page with a valid, unexpired token
- And the password field is left empty
- When the user clicks the "Reset Password" button
- Then the form should not submit
- And a validation message should be displayed for the password field

**Test Data:**
- New Password: (empty)

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-16

---

### TC-034: Positive - Previous Tokens Invalidated After Successful Reset
**Test Type:** Positive  
**Feature:** Forgot Password  
**Description:** All previous reset tokens should be invalidated after successful password reset  
**Scenario:** Verify token invalidation after password reset  
**Test Steps:**
- Given the user has successfully reset password using Token A
- And there were previously multiple reset tokens (Token B, Token C) generated for this user
- When the user tries to use Token B to reset password again
- Then the system should display an error message "This reset link is no longer valid"
- And all previous reset tokens should be invalidated

**Test Data:**
- Token A: Used for successful reset (invalidated)
- Token B: Previous token (invalidated)
- Token C: Previous token (invalidated)

**Priority:** High  
**Severity:** High  
**Requirement Link:** FR-18

---

## Rate Limiting Tests

### TC-035: Negative - Brute Force Attack Prevention
**Test Type:** Security/Negative  
**Feature:** Rate Limiting  
**Description:** System should implement rate limiting to prevent brute force attacks  
**Scenario:** Multiple rapid login attempts from same IP  
**Test Steps:**
- Given a user is attempting multiple rapid login requests from the same IP address
- When the user makes more than 10 login attempts within 1 minute from the same IP
- Then the system should block further login attempts
- And a rate limiting error message should be displayed
- And the IP should be temporarily blocked from accessing the login endpoint

**Test Data:**
- IP Address: 192.168.1.1
- Attempts: 11 in 1 minute (exceeds limit of 10)

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** NFR-7

---

## Session Management Tests

### TC-036: Positive - Session Created After Successful Login
**Test Type:** Positive  
**Feature:** Session Management  
**Description:** User session should be created after successful login  
**Scenario:** Session creation on successful authentication  
**Test Steps:**
- Given the user is on the Login page
- And the user has entered valid credentials
- When the user clicks the Login button and authenticates successfully
- Then a secure session should be created for the user
- And a session token/cookie should be stored
- And the user should remain logged in while browsing

**Test Data:**
- Email: testuser@example.com
- Password: Password@123

**Priority:** High  
**Severity:** High  
**Requirement Link:** NFR-8

---

### TC-037: Negative - Session Not Created for Failed Login
**Test Type:** Negative  
**Feature:** Session Management  
**Description:** No session should be created for failed login attempts  
**Scenario:** Failed login does not create session  
**Test Steps:**
- Given the user is on the Login page
- And the user has entered invalid credentials
- When the user clicks the Login button and login fails
- Then no session should be created
- And no session token/cookie should be stored
- And the user should remain unauthenticated

**Test Data:**
- Email: testuser@example.com
- Password: WrongPassword@123

**Priority:** High  
**Severity:** High  
**Requirement Link:** NFR-8

---

## Performance Tests

### TC-038: Performance - Login Authentication Response Time
**Test Type:** Performance  
**Feature:** Performance  
**Description:** Login authentication should complete within 2 seconds  
**Scenario:** Verify login response time  
**Test Steps:**
- Given the user is on the Login page
- And the user has entered valid credentials
- When the user clicks the Login button
- Then the authentication process should complete within 2 seconds
- And the user should be redirected to the Dashboard within the time limit

**Test Data:**
- Email: testuser@example.com
- Password: Password@123
- Expected Response Time: <= 2 seconds

**Priority:** Medium  
**Severity:** Medium  
**Requirement Link:** NFR-3

---

### TC-039: Performance - Password Reset Email Delivery
**Test Type:** Performance  
**Feature:** Performance  
**Description:** Password reset email should be delivered within 5 seconds of request  
**Scenario:** Verify email delivery performance  
**Test Steps:**
- Given the user is on the Forgot Password page
- And the user has entered a valid registered email
- When the user clicks the "Send Reset Link" button
- Then the password reset email should be sent within 5 seconds
- And the user should receive a success message within the time limit

**Test Data:**
- Email: testuser@example.com
- Expected Delivery Time: <= 5 seconds

**Priority:** Medium  
**Severity:** Medium  
**Requirement Link:** NFR-4

---

## Security & Auditability Tests

### TC-040: Security - HTTPS/TLS Communication
**Test Type:** Security  
**Feature:** Security  
**Description:** All credential communication should use HTTPS/TLS  
**Scenario:** Verify secure communication protocol  
**Test Steps:**
- Given the user is accessing the authentication system
- When the user submits login or password reset credentials
- Then all communication should be encrypted using HTTPS/TLS protocol
- And the connection should display a secure/valid SSL certificate indicator

**Test Data:** N/A

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** NFR-2

---

### TC-041: Security - Password Hashing
**Test Type:** Security  
**Feature:** Security  
**Description:** Passwords should be hashed using industry-standard encryption (bcrypt)  
**Scenario:** Verify password storage security  
**Test Steps:**
- Given a new user password "Password@123" is created during registration or reset
- When the password is stored in the database
- Then the password should be hashed using bcrypt with strength 12
- And the plain text password should never be stored
- And the stored hash should not be reversible to the original password

**Test Data:**
- Plain Password: Password@123
- Expected Hash: bcrypt hash with strength 12

**Priority:** High  
**Severity:** Critical  
**Requirement Link:** NFR-1

---

### TC-042: Auditability - Failed Login Audit Log
**Test Type:** Auditability  
**Feature:** Auditability  
**Description:** All failed login attempts should be logged for security auditing  
**Scenario:** Failed login attempts are logged  
**Test Steps:**
- Given a user attempts to login with invalid credentials
- When the login fails
- Then the failed attempt should be logged in the audit log with:
  - User email/identifier
  - Timestamp
  - IP address
  - Browser/User-Agent
  - Failure reason
- And the audit log entry should be persisted for compliance review

**Test Data:**
- Email: testuser@example.com
- Failed Password: WrongPassword@123
- IP Address: 192.168.1.1

**Priority:** High  
**Severity:** High  
**Requirement Link:** NFR-5

---

### TC-043: Auditability - Account Lockout Audit Log
**Test Type:** Auditability  
**Feature:** Auditability  
**Description:** Account lockout events should be logged for security auditing  
**Scenario:** Account lockout is audited  
**Test Steps:**
- Given a user's account is locked after 3 failed login attempts
- When the account is locked
- Then the lockout event should be logged in the audit log with:
  - User email/identifier
  - Timestamp of lockout
  - Number of failed attempts
  - IP address
  - Expected unlock time
- And the audit log entry should be persisted for compliance review

**Test Data:**
- Email: testuser@example.com
- Failed Attempts: 3
- Lockout Time: Current timestamp
- Unlock Time: Current timestamp + 1 minute

**Priority:** High  
**Severity:** High  
**Requirement Link:** NFR-5

---

## Error Message Tests

### TC-044: UI/UX - Error Message Not Revealing User Existence
**Test Type:** UI/UX  
**Feature:** Error Messages  
**Description:** Error messages should not reveal whether user exists or not  
**Scenario:** Generic error message for non-existent user  
**Test Steps:**
- Given a user attempts to login with a non-existent email
- When the login fails
- Then the error message should be "Invalid credentials"
- And the error message should NOT say "User does not exist"
- And the error message should NOT reveal any information about user existence
- And the error message should be identical to the message for invalid password

**Test Data:**
- Email: nonexistent@example.com
- Password: Password@123

**Priority:** High  
**Severity:** High  
**Requirement Link:** NFR-6

---

### TC-045: UI/UX - Account Locked Error Message
**Test Type:** UI/UX  
**Feature:** Error Messages  
**Description:** Clear error message when account is locked  
**Scenario:** Account locked error message  
**Test Steps:**
- Given a user's account is locked
- When the user attempts to login
- Then a clear error message "Your account has been locked due to multiple failed login attempts. Please try again after 1 minute" should be displayed
- And the error message should indicate when the account will be unlocked
- And the message should not be vague or confusing

**Test Data:**
- Email: lockeduser@example.com
- Password: Password@123

**Priority:** Medium  
**Severity:** Medium  
**Requirement Link:** FR-9, NFR-6

---

