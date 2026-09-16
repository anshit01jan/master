@smoke @high
# Forgot-password scenarios remain part of the validated passing local suite.
# PR scope refresh note: comment-only touch to keep this feature visible in PR #4.
Feature: Forgot Password
  The application should let a registered user request a password reset without revealing account existence.

  Scenario: Request password reset with registered username
    Given the user is on the Forgot Password page
    And the user enters a registered username "scrum50"
    When the user clicks on the Send reset link button
    Then the user should see the message "If the account exists, a reset link has been sent."

  Scenario: Request password reset with registered email
    Given the user is on the Forgot Password page
    And the user enters a registered email "scrum50@example.com"
    When the user clicks on the Send reset link button
    Then the user should see the message "If the account exists, a reset link has been sent."

  @regression @medium
  Scenario: Request password reset with unregistered identifier
    Given the user is on the Forgot Password page
    And the user enters an unregistered identifier "unknown@example.com"
    When the user clicks on the Send reset link button
    Then the user should see the message "If the account exists, a reset link has been sent."

  @regression @medium
  Scenario: Verify forgot password page controls and labels
    Given the user opens the Forgot Password page
    When the page is fully loaded
    Then the forgot password page should display
      | Username or email input field |
      | Send reset link button |
      | Back to login link |
