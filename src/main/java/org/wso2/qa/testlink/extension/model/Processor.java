package org.wso2.qa.testlink.extension.model;

import br.eti.kinoshita.testlinkjavaapi.model.CustomField;
import br.eti.kinoshita.testlinkjavaapi.model.Platform;
import br.eti.kinoshita.testlinkjavaapi.model.TestCase;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Set test execution status in each test case, based on the results
 */

public class Processor {

    private static final Logger LOGGER = Logger.getLogger(Processor.class);


    private Map<String, List<TestResult>> testResults = null;
    private TestCase[] testCases = null;
    private Platform[] platforms = null;
    private Configurations configurations = Configurations.getInstance();

    public Processor(Map<String, List<TestResult>> testResults, TestCase[] testCases, Platform[] platforms) {
        this.testResults = testResults;
        this.testCases = testCases;
        this.platforms = platforms;
    }

    public List<TestResult> getProcessedResults() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(String.format("Started processing %d test cases and %d rest results for %d platforms.",
                    testCases.length, testResults.size(), platforms.length));
        }

        List<TestResult> testCasesWithResults = new ArrayList<TestResult>();

        for (TestCase testCase : testCases) {

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Processing ... \n\t" + testCase);
            }

            if (testCase != null && testCase.getCustomFields() != null && !testCase.getCustomFields().isEmpty()) {

                String unitTestMethodFieldValue = getUnitTestMethodFieldValue(testCase);
                String integrationTestMethodFieldValue = getIntegrationTestMethodName(testCase);
                String uiTestMethodFieldValue = getIntegrationTestMethodName(testCase);

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Value of the custom field for unit test methods : " + unitTestMethodFieldValue);
                    LOGGER.debug("Value of the custom field for integration test methods : " + integrationTestMethodFieldValue);
                }

                boolean areIntegrationTestsAvailable = areIntegrationTestsAvailable(integrationTestMethodFieldValue);
                boolean areUnitTestsAvailable = areUnitTestsAvailable(unitTestMethodFieldValue);
                boolean areUITestsAvailable = isUITestsAvailable(uiTestMethodFieldValue);

                // If either unit test or integration test are not available do not process this test case.
                if (!areIntegrationTestsAvailable && !areUnitTestsAvailable && !areUITestsAvailable) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("No Tests mapped to the testcase. Skipping test case result update.");
                    }
                    continue;
                }

                String overallResultStatus = Constants.PASS;
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Evaluating the result of the test case. Initial (assumed) result : " + overallResultStatus);
                }

                //Find overall result for unit test
                if (areUnitTestsAvailable) {

                    String[] unitTestMethods = unitTestMethodFieldValue.split(",");

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("\tEvaluating unit test results");
                    }

                    for (String unitTestMethod : unitTestMethods) {

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("\t\tUnit test method name : " + unitTestMethod);
                        }

                        List<TestResult> unitTestResults = testResults.get(unitTestMethod);
                        if (unitTestResults != null && !unitTestResults.isEmpty()) {

                            String unitTestResult = unitTestResults.get(0).getStatus();

                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("\t\t\tUnit test result : " + unitTestResult);
                            }

                            if (!(Constants.PASS.equals(unitTestResult))) {
                                overallResultStatus = unitTestResults.get(0).getStatus();
                            }
                            if (Constants.FAIL.equals(unitTestResult)) {
                                if (LOGGER.isDebugEnabled()) {
                                    LOGGER.debug("\t\t\tNo further result evaluation is needed since one failed " +
                                            "test method has been found.");
                                }
                                break;
                            }
                        } else {
                            // When there no results found for the unit test method.
                            overallResultStatus = Constants.SKIP;

                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("\t\t\tCan't find result of the unit test method");
                            }

                        }

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("\t\t\tCurrent result : " + overallResultStatus);
                        }

                    }
                }

                // Now we have the overall test result for unit tests, or "PASSED" if there are no unit tests..

                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("\t\tFinal unit tests evaluation result : " + overallResultStatus);
                }


                // If there are no integration tests available, update the test case with the current overall result, only for "not-specified" platform
                if (!areIntegrationTestsAvailable) {

                    TestResult result = new TestResult();
                    result.setPlatform("NOT_SPECIFIED");
                    result.setTestCaseId(testCase.getId());
                    result.setStatus(overallResultStatus);
                    testCasesWithResults.add(result);

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("\t\tConcluding the test result evaluation since there are " +
                                "no integrations test available for the test case");
                        LOGGER.debug("\t\t** Final result ** : " + result);
                    }

                    // Do not proceed.
                    continue;
                } else {

                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("\tEvaluating integration test results");
                    }

                    String[] integrationTestMethods = integrationTestMethodFieldValue.split(",");

                    // We need to preserve the overall unit test result to make sure that ,
                    // every platform result evaluation start with the same unit test result.
                    String unitTestOverallRestStatus = overallResultStatus;
                    boolean isTestResultForPlatformAvailable;

                    for (Platform platform : platforms) {

                        // Init few flags for the new platform result evaluation.
                        isTestResultForPlatformAvailable = false;
                        overallResultStatus = unitTestOverallRestStatus;

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("\t\tEvaluating for the platform : " + platform);
                            LOGGER.debug("\t\t\tCurrent result (inherited from unit tests if there were any) : " + overallResultStatus);
                        }

                        outerLoop:
                        for (String integrationTestMethod : integrationTestMethods) {

                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("\t\t\tIntegration test method name : " + integrationTestMethod);
                            }

                            //If the test result method is not blank
                            if (StringUtils.isNotBlank(integrationTestMethod)) {

                                List<TestResult> integrationTestResultsForPlatforms = testResults.get(integrationTestMethod);
                                if (integrationTestResultsForPlatforms != null && !integrationTestResultsForPlatforms.isEmpty()) {
                                    for (TestResult testResult : integrationTestResultsForPlatforms) {
                                        if (platform.getName().equals(testResult.getPlatform())) {

                                            String integrationTestResult = testResult.getStatus();

                                            if (LOGGER.isDebugEnabled()) {
                                                LOGGER.debug("\t\t\t\tIntegration test result : " + integrationTestResult);
                                            }

                                            isTestResultForPlatformAvailable = true;

                                            if (!testResult.getStatus().equals(Constants.PASS)) {
                                                overallResultStatus = testResult.getStatus();
                                            }
                                            if (testResult.getStatus().equals(Constants.FAIL)) {

                                                if (LOGGER.isDebugEnabled()) {
                                                    LOGGER.debug("\t\t\t\tNo further result evaluation is needed since one failed " +
                                                            "test method has been found.");
                                                }

                                                break outerLoop;
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        //Update the platform result, if there are test cases available for the platform.
                        if (isTestResultForPlatformAvailable) {
                            TestResult result = new TestResult();
                            result.setPlatform(platform.getName());
                            result.setStatus(overallResultStatus);
                            result.setTestCaseId(testCase.getId());
                            testCasesWithResults.add(result);

                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.debug("\t\t\tConcluding the test result evaluation for the platform.");
                                LOGGER.debug("\t\t\t** Final result ** : " + result);
                            }


                        }
                    }
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Test Case '%s' doesn't have custom fields. Skipping the test case.", testCase.getFullExternalId()));
                }
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

    private boolean isUITestsAvailable(String uiTestMethodFieldValue) {
        return StringUtils.isNotBlank(uiTestMethodFieldValue);
    }

    private String getUnitTestMethodFieldValue(TestCase testCase) {

        for (CustomField customField : testCase.getCustomFields()) {
            if (configurations.getUnitTestCustomFieldName().equals(customField.getName())) {
                return customField.getValue();
            }
        }
        return null;
    }

    private String getIntegrationTestMethodName(TestCase testCase) {

        for (CustomField customField : testCase.getCustomFields()) {
            if (configurations.getIntegrationTestCustomFieldName().equals(customField.getName())) {
                return customField.getValue();
            }
        }
        return null;
    }

    private String getUITestMethodName(TestCase testCase) {
        for (CustomField customField : testCase.getCustomFields()) {
            if (configurations.getUiTestCustomFieldName().equals(customField.getName()))
                return customField.getValue();
        }
        return null;
    }
}
