package org.wso2.qa.testlink.extension.model;

import br.eti.kinoshita.testlinkjavaapi.model.CustomField;
import br.eti.kinoshita.testlinkjavaapi.model.Platform;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Set test execution status in each test case, based on the results
 */

public class Processor {

    private  Map<String, List<TestResult>> testResults = null;
    private  TestCase[] testCases = null;
    private  Platform[] platforms = null;

    public Processor(Map<String, List<TestResult>> testResults, TestCase[] testCases, Platform[] platforms) {
        this.testResults = testResults;
        this.testCases = testCases;
        this.platforms = platforms;
    }

    public List<TestResult> getProcessedResults() {

        List <TestResult> testCasesWithResults = new ArrayList<TestResult>();

        for(TestCase testCase : testCases){
            if(testCase != null && testCase.getCustomFields() != null && !testCase.getCustomFields().isEmpty()){

                String unitTestMethodFieldValue = getUnitTestMethodFieldValue(testCase);
                String integrationTestMethodFieldValue = getIntegrationTestMethodName(testCase);

                boolean areIntegrationTestsAvailable = areIntegrationTestsAvailable(integrationTestMethodFieldValue);
                boolean areUnitTestsAvailable = areUnitTestsAvailable(unitTestMethodFieldValue);

                // If either unit test or integration test are not available do not process this test case.
                if(!areIntegrationTestsAvailable && !areUnitTestsAvailable){
                    continue;
                }

                String overallResultStatus = Constants.PASS;

                //Find overall result for unit test
                if(areUnitTestsAvailable){

                    String[] unitTestMethods = unitTestMethodFieldValue.split(",");

                    for (String unitTestMethod : unitTestMethods ){

                        List<TestResult> unitTestResults = testResults.get(unitTestMethod);
                        if(unitTestResults != null && !unitTestResults.isEmpty()){

                            if(!(unitTestResults.get(0).getStatus().equals(Constants.PASS))){
                                overallResultStatus = unitTestResults.get(0).getStatus();
                            }
                            if (unitTestResults.get(0).getStatus().equals(Constants.FAIL)){
                                break;
                            }
                        }
                    }
                }

                // Now we have the overall test result for unit tests, or "PASSED" if there are no unit tests..

                // If there are no integration tests available, update the test case with the current overall result, only for "not-specified" platform
                if(!areIntegrationTestsAvailable){
                    TestResult result = new TestResult();
                    result.setPlatform("NOT_SPECIFIED");
                    result.setTestCaseId(testCase.getId());
                    result.setStatus(overallResultStatus);
                    testCasesWithResults.add(result);

                    // Do not proceed.
                    continue;
                }else{
                    String[] integrationTestMethods = integrationTestMethodFieldValue.split(",");

                    // We need to keep the overall unit test result to make sure that ,
                    // every platform result evaluation start with the same unit test result.
                    String unitTestOverallRestStatus = overallResultStatus;
                    boolean isTestResultForPlatformAvailable;

                    for (Platform platform : platforms){

                        // Init few flags for the new platform result evaluation.
                        isTestResultForPlatformAvailable = false;
                        overallResultStatus = unitTestOverallRestStatus;

                        outerLoop:
                        for (String integrationTestMethod : integrationTestMethods){
                            //If the test result method is not blank
                            if(StringUtils.isNotBlank(integrationTestMethod)){
                                for (TestResult testResult : testResults.get(integrationTestMethod)){
                                    if (platform.getName().equals(testResult.getPlatform())){
                                        isTestResultForPlatformAvailable = true;
                                        if(!testResult.getStatus().equals(Constants.PASS)){
                                            overallResultStatus = testResult.getStatus();
                                        }
                                        if (testResult.getStatus().equals(Constants.FAIL)){
                                            break outerLoop;
                                        }
                                    }
                                }
                            }
                        }
                        //Update the platform result, if there are test cases available for the platform.
                        if (isTestResultForPlatformAvailable){
                            TestResult result = new TestResult();
                            result.setPlatform(platform.getName());
                            result.setStatus(overallResultStatus);
                            result.setTestCaseId(testCase.getId());
                            testCasesWithResults.add(result);
                        }
                    }
                }
            }else {
                System.out.println("No test cases available for mapping");
            }
        }
        return testCasesWithResults;
    }

    private boolean areIntegrationTestsAvailable(String integrationTestMethodFieldValue) {
        return StringUtils.isNotBlank(integrationTestMethodFieldValue);
    }

    private boolean areUnitTestsAvailable(String unitTestMethodFieldValue) {
        return StringUtils.isNotBlank(unitTestMethodFieldValue);
    }

    private String getUnitTestMethodFieldValue(TestCase testCase) {

        for (CustomField customField : testCase.getCustomFields()){
            if (Constants.CUSTOM_FIELD_UNIT_TEST.equals(customField.getName())){
                return customField.getValue();
            }
        }
        return null;
    }

    private String getIntegrationTestMethodName(TestCase testCase) {

        for (CustomField customField : testCase.getCustomFields()){
            if (Constants.CUSTOM_FIELD_INTEGRATION_TEST.equals(customField.getName())){
                return customField.getValue();
            }
        }
        return null;
    }
}
