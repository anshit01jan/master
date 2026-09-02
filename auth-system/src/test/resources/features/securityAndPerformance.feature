@SecurityPerformanceModule
Feature: Security, Performance, Rate Limiting, Session Management and Error Handling
  As a system
  I want to ensure security, performance, and proper error handling
  So that the authentication system is robust and protected

  @regression @TC-035
  Scenario: Rate limiting prevents brute force attacks
    Given a user is attempting multiple rapid login requests
    When the user makes more than 10 login attempts within 1 minute from same IP
    Then the system should block further login attempts
    And a rate limiting error should be displayed
    And the IP should be temporarily blocked from login endpoint

  @regression @TC-036
  Scenario: Session created after successful login
    Given the user is on the Login page
    When the user enters valid credentials and clicks Login
    Then a secure session should be created
    And a session token/cookie should be stored
    And the user should remain logged in while browsing

  @regression @TC-037
  Scenario: Session not created for failed login
    Given the user is on the Login page
    When the user enters invalid credentials and clicks Login
    Then no session should be created
    And no session token/cookie should be stored
    And the user should remain unauthenticated

  @regression @TC-038
  Scenario: Login authentication response time
    Given the user is on the Login page
    When the user enters valid credentials and clicks Login
    Then authentication should complete within 2 seconds
    And the user should be redirected to Dashboard within time limit

  @regression @TC-039
  Scenario: Password reset email delivery time
    Given the user is on the Forgot Password page
    When the user enters valid email and clicks Send Reset Link
    Then the password reset email should be sent within 5 seconds
    And the user should receive success message within time limit

  @regression @TC-040
  Scenario: HTTPS/TLS communication
    Given the user is accessing the authentication system
    When the user submits login or password reset credentials
    Then all communication should be encrypted using HTTPS/TLS
    And the connection should display secure SSL certificate indicator

  @regression @TC-041
  Scenario: Password hashing with bcrypt
    Given a new user password is created
    When the password is stored in the database
    Then the password should be hashed using bcrypt with strength 12
    And the plain text password should never be stored
    And the stored hash should not be reversible

  @regression @TC-042
  Scenario: Failed login audit log
    Given a user attempts to login with invalid credentials
    When the login fails
    Then the failed attempt should be logged with:
      | field       | value              |
      | email       | testuser@example.com |
      | timestamp   | Current time       |
      | ip_address  | 192.168.1.1        |
      | reason      | Invalid credentials |

  @regression @TC-043
  Scenario: Account lockout audit log
    Given a user's account is locked after failed attempts
    When the account is locked
    Then the lockout event should be logged with:
      | field           | value            |
      | email           | testuser@example.com |
      | failed_attempts | 3                |
      | ip_address      | 192.168.1.1      |
      | expected_unlock | After 1 minute   |

  @regression @TC-044
  Scenario: Error message not revealing user existence
    Given a user attempts to login with non-existent email
    When the login fails
    Then the error message should be "Invalid credentials"
    And the error should NOT reveal user doesn't exist
    And the message should be identical to invalid password message

  @regression @TC-045
  Scenario: Account locked error message is clear
    Given a user's account is locked
    When the user attempts to login
    Then a clear error message "Your account has been locked due to multiple failed login attempts" should be displayed
    And the message should indicate unlock time (after 1 minute)
