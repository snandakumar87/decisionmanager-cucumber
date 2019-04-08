package cucumberTest;

import com.myspace.cucumber_test_project.Transaction;
import com.myspace.cucumber_test_project.Transaction_Needs_Review;
import cucumber.api.DataTable;
import cucumber.api.Scenario;
import cucumber.api.java8.En;
import cucumberTest.model.RuleExecution;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

public class TransactionRulesStepDefinition extends RulesEngineExecution implements En {

    private Scenario scenario;

    private RuleExecution ruleExecution;


    public TransactionRulesStepDefinition() {

        Before((Scenario scn) -> {
            this.ruleExecution = new RuleExecution();
            this.scenario = scn;
            //Kie session for the GAV
            createKieSession("com.myspace", "Cucumber_Test_Project", "1.0.0");
        });


        Given("^a transaction exists with", (DataTable dataTable) -> {
            List<Transaction> transactionList = new ArrayList<>();
            List<List<String>> transactions = dataTable.raw();
            if (CollectionUtils.isNotEmpty(transactions)) {
                transactions.forEach(trn -> {
                    if (!trn.get(0).equalsIgnoreCase("transactionRefNum")) {
                        Transaction transaction = new Transaction();
                        transaction.setTransactionRefNum(trn.get(0));
                        transaction.setForeignCurrencyTransaction(new Boolean(trn.get(1)));
                        transaction.setTransactionAmount(new Float(trn.get(2)));
                        transactionList.add(transaction);
                    }
                });
            }
            if (null != transactionList) {
                ruleExecution.setFacts(transactionList);
            }
        });

        When("^rules get fired", () -> {
            fireRules(ruleExecution);
        });

        Then("^atleast '(\\d+)' rule is executed", (Integer count) -> {
            Assert.assertTrue(ruleExecution.getRulesFired().size() > count);
        });

        Then("^rules are fired", (DataTable dataTable) -> {
            List<List<String>> results = dataTable.raw();
            results.stream().forEach(x -> x.stream().forEach(res -> {
                Assert.assertTrue(ruleExecution.getRulesFired().contains(res));
            }));
        });
        And("^the following transactions are tagged as needs review$", (DataTable dataTable) -> {

        try {

            ruleExecution.setResultClassName("com.myspace.cucumber_test_project.Transaction_Needs_Review");
            RuleExecution ruleExec = verifyResults(ruleExecution);
            List<Transaction_Needs_Review> transactionList = new ArrayList<>();

            List<List<String>> transactions = dataTable.raw();
            if (CollectionUtils.isNotEmpty(transactions)) {
                transactions.forEach(trn -> {
                            if (!trn.get(0).equalsIgnoreCase("transactionRefNum")) {
                                Transaction_Needs_Review transaction_needs_review = new Transaction_Needs_Review();
                                transaction_needs_review.setNeedsReview(new Boolean(trn.get(1)));
                                transaction_needs_review.setTransactionRefNumber(trn.get(0));
                                transactionList.add(transaction_needs_review);
                            }
                        });

                  ruleExecution.getResults().stream().forEach(x-> {
                      Transaction_Needs_Review transaction_needs_review = (Transaction_Needs_Review)x;
                      transactionList.stream().forEach(y-> {
                          if(y.getTransactionRefNumber().equalsIgnoreCase(transaction_needs_review.getTransactionRefNumber())) {
                              Assert.assertTrue(y.getNeedsReview().equals(transaction_needs_review.getNeedsReview()));
                          }
                      });
                  });


            } }catch (Exception e) {
            e.printStackTrace();
        }
        });
        And("^no rules are fired$", () -> {
           Assert.assertTrue(null != ruleExecution.getRulesFired() || ruleExecution.getRulesFired().isEmpty());
        });
    }
}
