package org.wso2.qa.testlink.extension.model;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Set test execution status in each test case, based on the results
 */
public class Processor {

    private  Map<String, List<TestResult>> testResults = null;
    private  TestCase[] testCases = new TestCase[0];

    public Processor(Map<String, List<TestResult>> testResults, TestCase[] testCases) {
        this.testResults = testResults;
        this.testCases = testCases;
    }

    public List<TestResult> getProcessedResults() {
        List <TestResult> testCasesWithResults = new ArrayList<TestResult>();

        for(TestCase testCase : testCases){

            // TODO : Fix the possible NPE.
            if(testCase!= null && !(ArrayUtils.isEmpty(testCase.getCustomFields().toArray()))){

                String testMethod = getIntegrationTestMethodName(testCase);

                List<TestResult> testResultsForPlatforms = this.testResults.get(testMethod);

                if (!testResultsForPlatforms.isEmpty()){

                    for (TestResult result : testResultsForPlatforms ){

                        result.setTestCaseId(testCase.getId());
                        result.setPlatform(result.getPlatform());
                        result.setStatus(result.getStatus());
                        testCasesWithResults.add(result);

                    }
                }
            }else {
                System.out.println("No test cases available for mapping");
            }

        }

        return testCasesWithResults;
    }

    private String getIntegrationTestMethodName(TestCase testCase) {
        // TODO : Get the custom field name by name rather than getting the first element of the list.
        return testCase.getCustomFields().get(0).getValue();
    }
}
