@LoginModule
Feature: Login Functionality
  As a user
  I want to be able to login to the application
  So that I can access the dashboard

  Background:
    Given the user is on the login page

  @smoke @TC-001
  Scenario: Successful login with valid credentials
    When the user enters valid credentials
      | email                  | password     |
      | testuser@example.com   | Password@123 |
    And the user clicks the Login button
    Then the user should be redirected to the dashboard
    And the user should see a welcome message
    And the session should be active

  @regression @TC-002
  Scenario: Login with invalid password
    When the user enters credentials with invalid password
      | email                  | password        |
      | testuser@example.com   | WrongPassword   |
    And the user clicks the Login button
    Then the login should fail
    And an error message "Invalid credentials" should be displayed
    And the user should remain on the login page

  @regression @TC-003
  Scenario: Login with non-existent user
    When the user enters non-existent user credentials
      | email                     | password     |
      | nonexistent@example.com   | Password@123 |
    And the user clicks the Login button
    Then the login should fail
    And an error message "Invalid credentials" should be displayed
    And the user should remain on the login page

  @regression @TC-004
  Scenario: Login with empty email field
    When the user leaves the email field empty
    And enters password "Password@123"
    And the user clicks the Login button
    Then the login should fail
    And a validation error "Email is required" should be displayed
    And the Login button should remain active

  @regression @TC-005
  Scenario: Login with empty password field
    When the user enters email "testuser@example.com"
    And leaves the password field empty
    And the user clicks the Login button
    Then the login should fail
    And a validation error "Password is required" should be displayed
    And the Login button should remain active

  @regression @TC-006
  Scenario: Login with empty email and password fields
    When the user leaves both email and password fields empty
    And the user clicks the Login button
    Then the login should fail
    And validation errors should be displayed
      | field    | error                 |
      | email    | Email is required     |
      | password | Password is required  |
    And the Login button should remain active

  @regression @TC-007
  Scenario: SQL injection attempt in login
    When the user attempts SQL injection in email field
      | email                               | password     |
      | admin'--                            | anypassword  |
    And the user clicks the Login button
    Then the login should fail
    And the system should sanitize the input
    And an error message "Invalid credentials" should be displayed
    And the attempt should be logged in audit logs

  @regression @TC-008
  Scenario: Login with whitespace in credentials
    When the user enters credentials with leading and trailing whitespace
      | email                      | password         |
      |  testuser@example.com      |  Password@123    |
    And the user clicks the Login button
    Then the system should trim whitespace
    And the user should be redirected to the dashboard
    And the session should be active

  @regression @TC-009
  Scenario: Case insensitive email validation
    When the user enters email with different case
      | email                  | password     |
      | TestUser@Example.Com   | Password@123 |
    And the user clicks the Login button
    Then the system should treat email as case-insensitive
    And the user should be redirected to the dashboard
    And the session should be active
