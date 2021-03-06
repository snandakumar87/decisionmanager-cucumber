package cucumberTest;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/features", glue={"cucumberTest"}, monochrome = true, plugin = {"json:target/cucumber/cucumber.json"},strict=true)
public class CucumberRunnerTest {}
