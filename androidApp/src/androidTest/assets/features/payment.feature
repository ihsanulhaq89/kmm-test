Feature: Payment processing

  Scenario: Successful payment processing
    Given a user enters recipient email "user@example.com", amount "100.0", and currency "USD"
    When they submit the payment
    Then the payment should be processed successfully
    And the transaction should be saved in Firestore

  Scenario: Payment validation failure
    Given a user enters recipient email "invalid-email", amount "-50.0", and currency "USD"
    When they submit the payment
    Then the payment should not be processed
    And an error message "Invalid payment details" should be shown

  Scenario: API failure during payment processing
    Given a user enters recipient email "fail@example.com", amount "75.0", and currency "EUR"
    And the API is configured to fail
    When they submit the payment
    Then the payment should not be processed
    And an error message "Payment Failed" should be shown
