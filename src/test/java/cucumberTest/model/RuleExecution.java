package cucumberTest.model;

import java.util.List;

public class RuleExecution {

    List<?> facts;
    List<?> results;
    List<String> rulesFired;
    String resultClassName;


    public String getResultClassName() {
        return resultClassName;
    }

    public void setResultClassName(String resultClassName) {
        this.resultClassName = resultClassName;
    }

    public List<?> getFacts() {
        return facts;
    }

    public void setFacts(List<?> facts) {
        this.facts = facts;
    }

    public List<?> getResults() {
        return results;
    }

    public void setResults(List<?> results) {
        this.results = results;
    }

    public List<String> getRulesFired() {
        return rulesFired;
    }

    public void setRulesFired(List<String> rulesFired) {
        this.rulesFired = rulesFired;
    }
}


