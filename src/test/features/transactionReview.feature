Feature: Transaction Analysis

  Scenario: Foreign Transactions over $100 needs review
    Given a transaction exists with
  | transactionRefNum | foreignCurrencyTransaction | transactionAmount |
  | 12345  	          | true  	                   |250.00             |

    When rules get fired
    Then atleast '1' rule is executed
    And rules are fired
    | TransactionRule |
    And the following transactions are tagged as needs review
    | transactionRefNum | needsReview | secondLevelApprovalRequired |
    | 12345             | true        | false                       |

  Scenario: Transactions over $1000 needs review
    Given a transaction exists with
      | transactionRefNum | foreignCurrencyTransaction | transactionAmount |
      | 34545  	          | false  	                   |1250.00            |

    When rules get fired
    Then atleast '1' rule is executed
    And rules are fired
      | LargeTransactions |
    And the following transactions are tagged as needs review
      | transactionRefNum | needsReview | secondLevelApprovalRequired |
      | 34545             | true        | true                        |

  Scenario: Transactions less than $1000 and is not foreign does not need review
    Given a transaction exists with
      | transactionRefNum | foreignCurrencyTransaction | transactionAmount |
      | 34545  	          | false  	                   |1250.00            |

    When rules get fired
    And no rules are fired


