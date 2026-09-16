@smoke @high
# Reset-password scenarios capture token validation behavior for the stabilized suite.
# PR scope refresh note: comment-only touch to keep this feature visible in PR #4.
Feature: Password Reset
  Password reset must enforce token validation and password complexity rules.

  Scenario: Open reset password page without a token
    Given the user opens the Reset Password page without a token
    When the page is fully loaded
    Then the user should see the error message "Reset token is required."
    And the Reset Password page should remain displayed

  Scenario: Open reset password page with invalid token
    Given the user opens the Reset Password page with token "invalid-token-value"
    When the page is fully loaded
    Then the user should see the error message "Reset token is invalid."
    And the Reset Password page should remain displayed
