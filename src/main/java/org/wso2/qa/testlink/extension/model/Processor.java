package org.wso2.qa.testlink.extension.model;

import br.eti.kinoshita.testlinkjavaapi.model.TestCase;

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


    public List<ExecutionResult> getProcessedResults() {
        List <ExecutionResult> testCasesWithResults = new ArrayList<ExecutionResult>();

        for(TestCase testCase : testCases){

            // null check
            if ((testCase.getCustomFields()!= null)&(testCase.getCustomFields().get(0).getValue()!=null)){
                String testMethod = testCase.getCustomFields().get(0).getValue();

                if (!(testResults.get(testCase.getCustomFields().get(0).getValue())).isEmpty()){
                    // iterate list for each
                    for (int i=0; i < (testResults.get(testMethod)).size();i++ ){
                        ExecutionResult executionResult = new ExecutionResult();
                        executionResult.setTestCaseId(testCase.getId());
                        executionResult.setPlatform(testResults.get(testMethod).get(i).getPlatform());
                        executionResult.setExecutionStatus(testResults.get(testMethod).get(i).getStatus());
                        testCasesWithResults.add(executionResult);
                        System.out.println("=== testCasesWithResults added for" + i + "==== \n");
                    }

                }
            }

        }

        return testCasesWithResults;
    }
}
