package cucumberTest;


import cucumberTest.model.RuleExecution;
import org.junit.Assert;
import org.kie.api.KieServices;
import org.kie.api.builder.ReleaseId;
import org.kie.api.event.rule.AfterMatchFiredEvent;
import org.kie.api.event.rule.DefaultAgendaEventListener;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class RulesEngineExecution {

    private static KieSession kieSession;

    protected void createKieSession(String groupId, String artifactId, String version){

        ReleaseId kReleaseId = KieServices.get().newReleaseId("com.myspace","Cucumber_Test_Project","1.0.0");
        KieContainer kieContainer = KieServices.get().newKieContainer(kReleaseId);
        kieSession = kieContainer.newKieSession();

    }




	protected RuleExecution fireRules(RuleExecution ruleExecution) {
		try {

			List<String> rulesFired = new ArrayList<>();
			kieSession.addEventListener(new DefaultAgendaEventListener() {

				//this event will be executed after the rule matches with the model data
				public void afterMatchFired(AfterMatchFiredEvent event) {
					super.afterMatchFired(event);
					rulesFired.add(event.getMatch().getRule().getName());
				}
			});
				for(Object factObjs: ruleExecution.getFacts()) {
					kieSession.insert(factObjs);
                    int fired = kieSession.fireAllRules();
					ruleExecution.setRulesFired(rulesFired);


				return ruleExecution;
			}

		} catch (Exception e) {
			Assert.fail(e.getMessage());
		}
		return null;
	}

	protected RuleExecution verifyResults(RuleExecution ruleExecution) {
        Class<?> act = null;
        List<Object> resultObjects = new ArrayList<>();
        try {
            act = Class.forName(ruleExecution.getResultClassName());
            Collection<?> transactionRslts =kieSession.getObjects(new ClassObjectFilter(act));
            for(Object object:transactionRslts) {
                resultObjects.add(object);
            }
            ruleExecution.setResults(resultObjects);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return ruleExecution;

    }
}
