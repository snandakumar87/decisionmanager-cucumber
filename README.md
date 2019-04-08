# Cucumber BDD Integration with Red Hat Decision Manager

Cucumber is a tool that supports Behavior Driven Development (BDD). 
It offers a way to write tests that anybody can understand, regardless of their technical knowledge. In BDD, 
users (business analysts, product owners) first write scenarios or acceptance tests that describes the behavior 
of the system from the customer's perspective, for review and sign-off by the product owners before 
developers write their codes.

This is a demonstration of how we can fool proof Rules Development/Authoring, ensuring we have a way by which we can catch changes 
to ruleset/data objects.

Cucumber is chosen for this demonstrating due to its business friendly behavior driven nature.

Required Dependencies:
=====================

This maven project, depends on the Decision Manager project, for its execution, The project can be found @
https://github.com/snandakumar87/Cucumber-DM-Rules


Setup & Execution:
==================

1) Build and Deploy the Decision Manager project.
2) Make sure to check the GAV for the rules artifact in the pom.xml and RulesEngineExecution.class
3) mvn clean package 
4) Test reports for the execution can be found under the project in the following location:
  target/cucumber/cucumber-html-reports

Transaction Analysis:
=====================

For the purpose of this demonstrating, we have considered the following scenario,

"If a transaction, is a foriegn transaction greater than $100 or a higher value transaction with a transaction amount > $1000,
we would like to mark transaction as needing review. Additionally, for any transaction which is over $1000, we would like to
mark second level review"

The above representation can be represented in business language as below:

Scenario: Foreign Transactions over $100 needs review

    Given a transaction exists with
    | transactionRefNum | foreignCurrencyTransaction | transactionAmount |
    | 12345             | true  	                 |250.00             |
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
      | 34545 | false  	                 |1250.00            |
      When rules get fired
      Then atleast '1' rule is executed
      And rules are fired
      | LargeTransactions |
      And the following transactions are tagged as needs review
      | transactionRefNum | needsReview | secondLevelApprovalRequired |
      | 34545             | true        | true                        |
      
   The Decision Manager rules project, implements this using 3 rules. 
   1) Guided Rule (without DSL)
   2) Guided Rule (with DSL)
   3) Guided Decision Table
   
   Each business scenario, can be mapped one or more rules for the purpose of BDD testing.
   
   Succesful run, produces the following reports.
   
   ![Cucumber Reports](https://github.com/snandakumar87/decisionmanager-cucumber/blob/master/cucumber_report.png)![Cucumber Reports](https://github.com/snandakumar87/decisionmanager-cucumber/blob/master/Cucumber_feature.png)![Cucumber Reports](https://github.com/snandakumar87/decisionmanager-cucumber/blob/master/Cucumber_stats.png)
   
   Let us now change a rule, lets change the "needs Review" condition to $500, build and deploy the latest rule package.
   And re-run the cucumber tests.
   
   ![Changed Rule](https://github.com/snandakumar87/decisionmanager-cucumber/blob/master/changed_rule.png)
   
   The cucumber report now shows the failures to BAU, as the rules have changed.
   
   ![cucumber report failure1](https://github.com/snandakumar87/decisionmanager-cucumber/blob/master/test_failure_cucumber.png)
  
   ![cucumber report failure2](https://github.com/snandakumar87/decisionmanager-cucumber/blob/master/cucumber_scenario_failure.png)
   
   Cucumber based testing, can also come handy when performing tests against versions of the Rule artifact, it can give a side by side view of the execution when pointing to two different versions.

   
