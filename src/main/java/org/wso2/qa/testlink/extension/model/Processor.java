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

            // null check
            if(testCase!= null && !(ArrayUtils.isEmpty(testCase.getCustomFields().toArray()))){

                String testMethod = testCase.getCustomFields().get(0).getValue();

                if (!(testResults.get(testMethod)).isEmpty()){
                    // iterate list for each
                    for (int i=0; i < (testResults.get(testMethod)).size();i++ ){
                        TestResult executionResult = new TestResult();
                        executionResult.setTestCaseId(testCase.getId());
                        executionResult.setPlatform(testResults.get(testMethod).get(i).getPlatform());
                        executionResult.setStatus(testResults.get(testMethod).get(i).getStatus());
                        testCasesWithResults.add(executionResult);

                    }

                }
            }else {
                System.out.println("No test cases available for mapping");
            }

        }

        return testCasesWithResults;
    }
}
