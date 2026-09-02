@ForgotPasswordModule
Feature: Forgot Password Functionality
  As a user
  I want to reset my password if I forget it
  So that I can regain access to my account

  Background:
    Given the user is on the login page

  @smoke @TC-017
  Scenario: Forgot Password link is visible on login page
    Then a "Forgot Password?" link should be visible
    And the link should be clickable

  @smoke @TC-018
  Scenario: Navigate to Forgot Password form
    When the user clicks the "Forgot Password?" link
    Then the user should be redirected to the Forgot Password page
    And a form to enter email address should be displayed

  @regression @TC-019
  Scenario: Valid email reset request
    Given the user is on the Forgot Password page
    When the user enters email "testuser@example.com"
    And the user clicks the "Send Reset Link" button
    Then a success message "Password reset link has been sent" should be displayed
    And a reset email should be sent to "testuser@example.com"
    And the reset token should have 2 minute expiration

  @regression @TC-020
  Scenario: Non-registered email reset request
    Given the user is on the Forgot Password page
    When the user enters email "nonexistent@example.com"
    And the user clicks the "Send Reset Link" button
    Then a success message should be displayed (for security)
    And no email should be sent

  @regression @TC-021
  Scenario: Empty email in Forgot Password form
    Given the user is on the Forgot Password page
    When the user leaves email field empty
    And the user clicks the "Send Reset Link" button
    Then the form should not submit
    And a validation error "Email is required" should be displayed

  @regression @TC-022
  Scenario: Invalid email format in Forgot Password form
    Given the user is on the Forgot Password page
    When the user enters invalid email "notanemail"
    And the user clicks the "Send Reset Link" button
    Then the form should not submit
    And a validation error "Please enter a valid email" should be displayed

  @regression @TC-023
  Scenario: Click reset link in email
    Given the user has received a password reset email
    And the email contains a valid reset link with token
    When the user clicks the reset link in the email
    Then the user should be redirected to the password reset page
    And a form to enter new password should be displayed

  @regression @TC-024
  Scenario: Expired reset link
    Given the user has received a password reset email
    And 2 minutes and 1 second have passed since token generation
    When the user tries to click the expired reset link
    Then an error message "Reset link has expired" should be displayed
    And the user should be redirected to the Forgot Password page

  @regression @TC-025
  Scenario: Invalid or tampered reset token
    Given the user is on a password reset page
    When the user provides an invalid/tampered token
    Then an error message "Invalid or expired reset link" should be displayed
    And password reset should not be allowed

  @regression @TC-026
  Scenario: New request invalidates previous token
    Given the user has received a password reset email with Token A
    And the user requests another password reset (Token B)
    When the user tries to use Token A to reset password
    Then an error message "This reset link is no longer valid" should be displayed
    And only Token B should be valid

  @regression @TC-027
  Scenario: Strong password reset
    Given the user is on password reset page with valid token
    When the user enters new password "NewPassword@123"
    And the user clicks the "Reset Password" button
    Then password reset should succeed
    And a success message "Password has been successfully reset" should be displayed
    And the user should be redirected to the Login page
    And the user should be able to login with new password

  @regression @TC-028
  Scenario: Weak password during reset
    Given the user is on password reset page with valid token
    When the user enters weak password "123456"
    And the user clicks the "Reset Password" button
    Then password reset should fail
    And an error describing password requirements should be displayed

  @regression @TC-029
  Scenario: Password missing uppercase letter
    Given the user is on password reset page with valid token
    When the user enters password "newpassword@123"
    And the user clicks the "Reset Password" button
    Then password reset should fail
    And an error indicating uppercase required should be displayed

  @regression @TC-030
  Scenario: Password missing lowercase letter
    Given the user is on password reset page with valid token
    When the user enters password "NEWPASSWORD@123"
    And the user clicks the "Reset Password" button
    Then password reset should fail
    And an error indicating lowercase required should be displayed

  @regression @TC-031
  Scenario: Password missing special character
    Given the user is on password reset page with valid token
    When the user enters password "NewPassword123"
    And the user clicks the "Reset Password" button
    Then password reset should fail
    And an error indicating special character required should be displayed

  @regression @TC-032
  Scenario: Password less than 8 characters
    Given the user is on password reset page with valid token
    When the user enters password "New@12"
    And the user clicks the "Reset Password" button
    Then password reset should fail
    And an error indicating minimum 8 characters required should be displayed

  @regression @TC-033
  Scenario: Empty password field in reset
    Given the user is on password reset page with valid token
    When the user leaves password field empty
    And the user clicks the "Reset Password" button
    Then the form should not submit
    And a validation error "Password is required" should be displayed

  @regression @TC-034
  Scenario: Previous tokens invalidated after successful reset
    Given the user has successfully reset password using Token A
    And there were previous tokens (Token B, Token C)
    When the user tries to use Token B to reset password again
    Then an error message "This reset link is no longer valid" should be displayed
