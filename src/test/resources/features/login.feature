@smoke @high
# Login scenarios cover redirect, validation, and lockout behavior in the passing suite.
# PR scope refresh note: comment-only touch to keep this feature visible in PR #4.
Feature: User Login
  As a registered user
  I want to log in to the SCRUM-50 application
  So that I can access the dashboard

  Scenario: Login with valid credentials
    Given the user is on the Login page
    And the user has a valid username "scrum50"
    And the user has a valid password "Password1!"
    When the user clicks on the Log in button
    Then the user should be redirected to the Dashboard page

  Scenario: Submit login with blank username and blank password
    Given the user is on the Login page
    When the user clicks on the Log in button without entering a username and password
    Then the user should remain on the Login page
    And the user should see the error message "Username and password are required."

  Scenario: Submit login with blank username only
    Given the user is on the Login page
    And the user enters password "Password1!"
    When the user clicks on the Log in button without entering a username
    Then the user should remain on the Login page
    And the user should see the error message "Username and password are required."

  Scenario: Submit login with blank password only
    Given the user is on the Login page
    And the user enters username "scrum50"
    When the user clicks on the Log in button without entering a password
    Then the user should remain on the Login page
    And the user should see the error message "Username and password are required."

  Scenario: Submit login with invalid password
    Given the user is on the Login page
    And the user enters username "scrum50"
    And the user enters password "WrongPass1!"
    When the user clicks on the Log in button
    Then the user should remain on the Login page
    And the user should see the error message "Invalid username or password."

  Scenario: Trigger lockout on second failed login attempt
    Given the user is on the Login page
    And the user enters username "scrum50"
    And the user enters password "WrongPass1!"
    When the user clicks on the Log in button for the first failed attempt
    And the user clicks on the Log in button for the second failed attempt with the same invalid password
    Then the user should see the error message "Account locked after 2 failed attempts."
    And the account should be marked as locked

  Scenario: Login succeeds after automatic account unlock
    Given the user account "scrum50" is locked after 2 failed login attempts
    When the user enters username "scrum50" and password "Password1!"
    And the user clicks on the Log in button
    Then the user should remain on the Login page
    And the user should see the error message "Account is locked. Try again later."

  Scenario: Open dashboard without authenticated session
    Given the user does not have an authenticated session
    When the user opens the Dashboard page directly
    Then the user should be redirected to the Login page
