@AccountLockoutModule
Feature: Account Lockout Functionality
  As a system
  I want to lock user accounts after multiple failed login attempts
  So that I can prevent brute force attacks

  Background:
    Given the user is on the login page
    And the account is not currently locked

  @regression @TC-010
  Scenario: First failed login attempt
    When the user enters invalid credentials
      | email                  | password        |
      | testuser@example.com   | WrongPassword1  |
    And the user clicks the Login button
    Then the login should fail
    And an error message "Invalid credentials" should be displayed
    And the failed attempt count should be 1
    And the account should not be locked

  @regression @TC-011
  Scenario: Second failed login attempt
    Given the user has 1 failed login attempt
    When the user enters invalid credentials again
      | email                  | password        |
      | testuser@example.com   | WrongPassword2  |
    And the user clicks the Login button
    Then the login should fail
    And an error message "Invalid credentials" should be displayed
    And the failed attempt count should be 2
    And the account should not be locked

  @regression @TC-012
  Scenario: Third failed login attempt triggers lockout
    Given the user has 2 failed login attempts
    When the user enters invalid credentials again
      | email                  | password        |
      | testuser@example.com   | WrongPassword3  |
    And the user clicks the Login button
    Then the login should fail
    And an error message "Account locked. Try again after 1 minute" should be displayed
    And the account should be locked
    And the lockout should be recorded in database

  @regression @TC-013
  Scenario: Login attempt on locked account
    Given the user account is locked
    When the user enters valid credentials
      | email                  | password     |
      | testuser@example.com   | Password@123 |
    And the user clicks the Login button
    Then the login should fail
    And an error message "Account locked" should be displayed
    And the user should not be redirected to dashboard

  @regression @TC-014
  Scenario: Automatic unlock after 1 minute
    Given the user account has been locked for 61 seconds
    When the user enters valid credentials
      | email                  | password     |
      | testuser@example.com   | Password@123 |
    And the user clicks the Login button
    Then the login should succeed
    And the user should be redirected to the dashboard
    And the account should be unlocked
    And the failed attempt count should be reset to 0

  @regression @TC-015
  Scenario: Account remains locked before 1 minute
    Given the user account has been locked for 30 seconds
    When the user enters valid credentials
      | email                  | password     |
      | testuser@example.com   | Password@123 |
    And the user clicks the Login button
    Then the login should fail
    And an error message "Account locked" should be displayed
    And the account should remain locked

  @regression @TC-016
  Scenario: Failed attempt counter reset after successful login
    Given the user has 2 failed login attempts
    And the account is not locked
    When the user enters valid credentials
      | email                  | password     |
      | testuser@example.com   | Password@123 |
    And the user clicks the Login button
    Then the login should succeed
    And the user should be redirected to the dashboard
    And the failed attempt count should be reset to 0
    And the lockout tracking should be cleared
